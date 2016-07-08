package io.github.thushear.recommender;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
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

import java.io.File;
import java.io.IOException;

/**
 * Created by kongming on 2016/7/8.
 */
public class IREvaluatorIntro {



    public static void main(String[] args) throws IOException, TasteException {

        RandomUtils.useTestSeed();

        final File modelFile = new File(RecommenderConstants.modelPath);

        DataModel dataModel = new FileDataModel(modelFile);

        //查准率 查全率评估
        RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();

        RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
            public Recommender buildRecommender(DataModel dataModel) throws TasteException {
                UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
                UserNeighborhood neighborhood = new NearestNUserNeighborhood(2,similarity,dataModel);

                return new GenericUserBasedRecommender(dataModel,neighborhood,similarity);
            }
        };
        // Evaluate precision and recall "at 2":

        IRStatistics statistics = evaluator.evaluate(recommenderBuilder,null,dataModel,null,10,GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD,1.0);

        System.out.println(statistics.getPrecision());
        System.out.println(statistics.getRecall());
    }



}
