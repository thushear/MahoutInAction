package io.github.thushear.clustering;

import org.apache.mahout.clustering.display.DisplayKMeans;
import org.apache.mahout.clustering.kmeans.Cluster;
import org.apache.mahout.clustering.kmeans.KMeansClusterer;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kongming on 2016/7/13.
 */
public class KMeansExample {


    public static void main(String[] args) throws Exception {

        List<Vector> sampleData = new ArrayList<Vector>();

        RandomPointsUtil.generateSamples(sampleData,400,1,1,3);
        RandomPointsUtil.generateSamples(sampleData,300,1,0,0.5);
        RandomPointsUtil.generateSamples(sampleData,300,0,2,0.1);

        int k = 3;

        List<Vector> randomPoints = RandomPointsUtil.chooseRandomPoints(sampleData,k);
        List<Cluster> clusters = new ArrayList<Cluster>();

        int clusterId = 0;
        for (Vector randomPoint : randomPoints) {
            clusters.add(new Cluster(randomPoint,clusterId++,new EuclideanDistanceMeasure()));
        }

        //簇个数为3 收敛阈值为0.01
        List<List<Cluster>> finalClusters = KMeansClusterer.clusterPoints(sampleData,clusters,new EuclideanDistanceMeasure(),3,0.01);

        for (Cluster cluster : finalClusters.get(finalClusters.size() - 1)) {
            System.out.println("Cluster id: " + cluster.getId() + " center: "
                    + cluster.getCenter().asFormatString());

        }

        DisplayKMeans.main(null);
    }



}
