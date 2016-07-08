package io.github.thushear.recommender;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.example.grouplens.GroupLensDataModel;
import org.apache.mahout.cf.taste.impl.eval.LoadEvaluator;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by kongming on 2016/7/8.
 */
public class GroupLensDataModelIntro {

    public static void main(String[] args) throws IOException, TasteException {
        DataModel model = new GroupLensDataModel(new File(RecommenderConstants.grouplens1M));
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood =
                new NearestNUserNeighborhood(100, similarity, model);
        Recommender recommender =
                new GenericUserBasedRecommender(model, neighborhood, similarity);
        LoadEvaluator.runLoad(recommender);

        List<RecommendedItem> recommendedItems = recommender.recommend(1,10);

        for (RecommendedItem recommendedItem : recommendedItems) {
            System.out.println(recommendedItem);
        }
    }
}
