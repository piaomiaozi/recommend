package com.wuage.recommend;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class UserCF {

	public final static int NEIGHBORHOOD_NUM = 2;
	public final static int RECOMMENDER_NUM = 3;

	public void recommendUser() throws IOException, TasteException {
		String fileName = "." + File.separator + "files" + File.separator
				+ "item.csv";
		DataModel model = new FileDataModel(new File(fileName));
		UserSimilarity user = new EuclideanDistanceSimilarity(model);
		NearestNUserNeighborhood neighbor = new NearestNUserNeighborhood(
				NEIGHBORHOOD_NUM, user, model);
		Recommender r = new GenericUserBasedRecommender(model, neighbor, user);
		LongPrimitiveIterator iter = model.getUserIDs();
		long uid = 0;
		while (iter.hasNext()) {
			uid = iter.nextLong();
			List<RecommendedItem> list = r.recommend(uid, RECOMMENDER_NUM);
			System.out.printf("uid:%s", uid);
			if (CollectionUtils.isEmpty(list)) {
				continue;
			}
			for (RecommendedItem item : list) {
				System.out.printf("(%s,%f)", item.getItemID(), item.getValue());
			}
			System.out.println();
		}
	}

	public static void main(String[] args) throws IOException, TasteException {
		UserCF userCF = new UserCF();
		userCF.recommendUser();
	}
}
