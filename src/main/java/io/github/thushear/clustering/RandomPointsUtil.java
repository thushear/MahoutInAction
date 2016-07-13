package io.github.thushear.clustering;

import org.apache.mahout.clustering.dirichlet.UncommonDistributions;
import org.apache.mahout.common.RandomUtils;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by kongming on 2016/7/13.
 */
public class RandomPointsUtil {


    /**
     * 生成向量集
     * @param vectors
     * @param num
     * @param mx
     * @param my
     * @param sd
     */
    public static void generateSamples(List<Vector> vectors, int num,double mx,double my,double sd){
        for (int i = 0; i < num; i++) {
            vectors.add(new DenseVector(new double[]{UncommonDistributions.rNorm(mx,sd),UncommonDistributions.rNorm(my,sd)}));
        }
    }

    /**
     * 随机选择k个中心点
     * @param vectors
     * @param k
     * @return
     */
    public static List<Vector> chooseRandomPoints(Iterable<Vector> vectors,int k){
        List<Vector> chosenPoints = new ArrayList<Vector>(k);
        Random random = RandomUtils.getRandom();
        for (Vector vector : vectors) {
            int currentSize = chosenPoints.size();
            if (currentSize < k){
                chosenPoints.add(vector);
            }else if (random.nextInt(currentSize + 1) == 0){// with chance 1/(currentSize+1) pick new element
                int indexToReomove = random.nextInt(currentSize);
                chosenPoints.add(vector);
            }
        }
        return chosenPoints;
    }

}
