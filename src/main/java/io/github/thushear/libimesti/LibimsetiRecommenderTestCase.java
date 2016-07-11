package io.github.thushear.libimesti;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.io.IOException;
import java.util.List;

/**
 * Created by kongming on 2016/7/11.
 */
public class LibimsetiRecommenderTestCase {


    public static void main(String[] args) throws TasteException, IOException {

        LibimsetiRecommender libimsetiRecommender = new LibimsetiRecommender();

        List<RecommendedItem> recommendedItems = libimsetiRecommender.recommend(16441l,2);
        for (RecommendedItem recommendedItem : recommendedItems) {
            System.out.println(recommendedItem);
        }
    }






}
