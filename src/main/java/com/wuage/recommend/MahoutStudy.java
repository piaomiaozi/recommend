package com.wuage.recommend;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

public class MahoutStudy {
	public final static String FILE_NAME = "." + File.separator + "files" + File.separator + "intro.csv";
	public final static String BIG_FILE_NAME = "." + File.separator + "files" + File.separator + "ua.base";
	
	public final static String BIG_FILE_NAME_1000K = "." + File.separator + "files" + File.separator + "ratings.dat";
	/**
	 * 根据数据模型计算推荐的差值
	 * 
	 * @throws IOException
	 * @throws TasteException
	 */
	public static void testEvaluate() throws IOException, TasteException {
		// RandomUtils.useTestSeed();
		DataModel model = new FileDataModel(new File(BIG_FILE_NAME));
		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		RecommenderBuilder builder = new RecommenderBuilder() {

			@Override
			public Recommender buildRecommender(DataModel dataModel) throws TasteException {
				UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, dataModel);
				return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
			}
		};
		double score = evaluator.evaluate(builder, null, model, 0.7, 1.0);
		System.out.println(score);

	}

	/**
	 * 计算查全率和查准率
	 * @throws IOException
	 * @throws TasteException
	 */
	public static void testRecommenderIRStatsEvaluator() throws IOException, TasteException {
		RandomUtils.useTestSeed();
		DataModel model = new FileDataModel(new File(BIG_FILE_NAME));
		RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
		RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {

			@Override
			public Recommender buildRecommender(DataModel dataModel) throws TasteException {
				UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, dataModel);
				return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
			}
		};
		IRStatistics stats = evaluator.evaluate(recommenderBuilder, null, model, null, 2,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
		System.out.println(stats.getPrecision());
		System.out.println(stats.getRecall());
	}
	/**
	 * 计算查全率和查准率
	 * @throws IOException
	 * @throws TasteException
	 */
	public static void testRecommenderIRStatsEvaluatorGroupLens() throws IOException, TasteException {
		RandomUtils.useTestSeed();
		DataModel model = new FileDataModel(new File(BIG_FILE_NAME_1000K));
		RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
		RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {

			@Override
			public Recommender buildRecommender(DataModel dataModel) throws TasteException {
				UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, dataModel);
				return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
			}
		};
		IRStatistics stats = evaluator.evaluate(recommenderBuilder, null, model, null, 2,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
		System.out.println(stats.getPrecision());
		System.out.println(stats.getRecall());
	}

	public static void main(String[] args) throws IOException, TasteException {
//		MahoutStudy.testEvaluate();
//		MahoutStudy.testRecommenderIRStatsEvaluator();
		MahoutStudy.testRecommenderIRStatsEvaluatorGroupLens();
	}
	//
	//

	//

	//

	//

	///

	//
	//
	//

	//
}
