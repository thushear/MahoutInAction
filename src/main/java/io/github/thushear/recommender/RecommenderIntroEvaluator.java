package io.github.thushear.recommender;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
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
 * 使用均差平均值评估推荐的好坏
 * 训练集 测试集
 */
public class RecommenderIntroEvaluator {



    public static void main(String[] args) throws IOException, TasteException {

        RandomUtils.useTestSeed();

        final File modelFile = new File(RecommenderConstants.modelPath);

        DataModel dataModel = new FileDataModel(modelFile);

        RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();

        RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
            public Recommender buildRecommender(DataModel dataModel) throws TasteException {
                UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
                UserNeighborhood neighborhood = new NearestNUserNeighborhood(2,similarity,dataModel);

                return new GenericUserBasedRecommender(dataModel,neighborhood,similarity);
            }
        };

        // Use 70% of the data to train; test using the other 30%.
        double score = evaluator.evaluate(recommenderBuilder,null,dataModel,0.7,1.0);

        System.out.println(score);



    }




}
