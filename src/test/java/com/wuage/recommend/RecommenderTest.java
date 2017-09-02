package com.wuage.recommend;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.recommender.svd.ALSWRFactorizer;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import com.wuage.recommend.factory.RecommendFactory;

public class RecommenderTest {

	public final static String FILE_NAME = "." + File.separator + "files" + File.separator + "item.csv";
	public final static int NEIGHBORHOOD_NUM = 2;
	/**
	 * 推荐个数
	 */
	public final static int RECOMMENDER_NUM = 3;

	/**
	 * 基于用户的协同过滤算法UserCF
	 * 
	 * @param dataModel
	 * @throws TasteException
	 */
	public static void userCF(DataModel dataModel) throws TasteException {
		UserSimilarity userSimilarity = RecommendFactory.userSimilarity(RecommendFactory.SIMILARITY.EUCLIDEAN,
				dataModel);
		UserNeighborhood userNeighborhood = RecommendFactory.userNeighborhood(RecommendFactory.NEIGHBORHOOD.NEAREST,
				userSimilarity, dataModel, NEIGHBORHOOD_NUM);
		RecommenderBuilder recommenderBuilder = RecommendFactory.userRecommender(userSimilarity, userNeighborhood,
				true);
		RecommendFactory.evaluate(RecommendFactory.EVALUATOR.AVERAGE_ABSOLUTE_DIFFERENCE, recommenderBuilder, null,
				dataModel, 0.7);
		RecommendFactory.statsEvaluator(recommenderBuilder, null, dataModel, 2);

		LongPrimitiveIterator iter = dataModel.getUserIDs();
		while (iter.hasNext()) {
			long uid = iter.nextLong();
			List<RecommendedItem> list = recommenderBuilder.buildRecommender(dataModel).recommend(uid, RECOMMENDER_NUM);
			RecommendFactory.showItems(uid, list, true);
		}
	}

	/**
	 * 基于物品的协同过滤算法ItemCF
	 * 
	 * @param dataModel
	 * @throws TasteException
	 */
	public static void itemCF(DataModel dataModel) throws TasteException {
		ItemSimilarity itemSimilarity = RecommendFactory.itemSimilarity(RecommendFactory.SIMILARITY.EUCLIDEAN,
				dataModel);
		RecommenderBuilder recommenderBuilder = RecommendFactory.itemRecommender(itemSimilarity, true);

		RecommendFactory.evaluate(RecommendFactory.EVALUATOR.AVERAGE_ABSOLUTE_DIFFERENCE, recommenderBuilder, null,
				dataModel, 0.7);
		RecommendFactory.statsEvaluator(recommenderBuilder, null, dataModel, 2);

		LongPrimitiveIterator iter = dataModel.getUserIDs();
		while (iter.hasNext()) {
			long uid = iter.nextLong();
			List<RecommendedItem> list = recommenderBuilder.buildRecommender(dataModel).recommend(uid, RECOMMENDER_NUM);
			RecommendFactory.showItems(uid, list, true);
		}
	}

	// public static void slopeOne(DataModel dataModel) throws TasteException {
	// RecommenderBuilder recommenderBuilder =
	// RecommendFactory.slopeOneRecommender();
	//
	// RecommendFactory.evaluate(RecommendFactory.EVALUATOR.AVERAGE_ABSOLUTE_DIFFERENCE,
	// recommenderBuilder, null, dataModel, 0.7);
	// RecommendFactory.statsEvaluator(recommenderBuilder, null, dataModel, 2);
	//
	// LongPrimitiveIterator iter = dataModel.getUserIDs();
	// while (iter.hasNext()) {
	// long uid = iter.nextLong();
	// List list = recommenderBuilder.buildRecommender(dataModel).recommend(uid,
	// RECOMMENDER_NUM);
	// RecommendFactory.showItems(uid, list, true);
	// }
	// }

	public static void svd(DataModel dataModel) throws TasteException {
		RecommenderBuilder recommenderBuilder = RecommendFactory
				.svdRecommender(new ALSWRFactorizer(dataModel, 10, 0.05, 10));

		RecommendFactory.evaluate(RecommendFactory.EVALUATOR.AVERAGE_ABSOLUTE_DIFFERENCE, recommenderBuilder, null,
				dataModel, 0.7);
		RecommendFactory.statsEvaluator(recommenderBuilder, null, dataModel, 2);

		LongPrimitiveIterator iter = dataModel.getUserIDs();
		while (iter.hasNext()) {
			long uid = iter.nextLong();
			List<RecommendedItem> list = recommenderBuilder.buildRecommender(dataModel).recommend(uid, RECOMMENDER_NUM);
			RecommendFactory.showItems(uid, list, true);
		}
	}

	public static void main(String[] args) throws TasteException, IOException {
		DataModel dataModel = RecommendFactory.buildDataModel(FILE_NAME);
		RecommenderTest.userCF(dataModel);
		RecommenderTest.itemCF(dataModel);
	}

}
