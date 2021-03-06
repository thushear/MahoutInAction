package io.github.thushear.clustering;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.clustering.WeightedVectorWritable;
import org.apache.mahout.clustering.kmeans.Cluster;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kongming on 2016/7/12.
 */
public class SimpleKMeansClustering {


    public static final double[][] points = { {1, 1}, {2, 1}, {1, 2},
            {2, 2}, {3, 3}, {8, 8},
            {9, 8}, {8, 9}, {9, 9}};


    public static void writePointsToFile(List<Vector> points, String filename, FileSystem fs,
                                         Configuration configuration) throws IOException {
        Path path = new Path(filename);
        SequenceFile.Writer writer = new SequenceFile.Writer(fs,configuration,path, LongWritable.class, VectorWritable.class);
        long recNum = 0;
        VectorWritable vec = new VectorWritable();
        for (Vector point : points) {
            vec.set(point);
            writer.append(new LongWritable(recNum++),vec);
        }
        writer.close();
    }


    public static List<Vector> getPoints(double[][] raw){
        List<Vector> points = new ArrayList<Vector>();
        for (int i = 0; i < raw.length; i++) {
            double[] fr = raw[i];
            Vector vec = new RandomAccessSparseVector(fr.length);
            vec.assign(fr);
            points.add(vec);
        }
        return points;
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {


        int k = 2;
        List<Vector> vectors = getPoints(points);

        File testData  = new File("testdata");
        if (!testData.exists()){
            testData.mkdir();
        }

        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(configuration);
        writePointsToFile(vectors,"testdata/points/file1",fileSystem,configuration);
        Path path = new Path("testdata/clusters/part-00000");
        SequenceFile.Writer writer = new SequenceFile.Writer(fileSystem,configuration,path, Text.class, Cluster.class);
        for (int i = 0; i < k; i++) {
            Vector vector = vectors.get(i);
            Cluster cluster = new Cluster(vector,i,new EuclideanDistanceMeasure());
            writer.append(new Text(cluster.getIdentifier()),cluster);
        }
        writer.close();

        KMeansDriver.run(configuration,new Path("testdata/points"),new Path("testdata/clusters"),new Path("output"),new EuclideanDistanceMeasure(), 0.001, 10,
                true, false);

        SequenceFile.Reader reader = new SequenceFile.Reader(fileSystem,
                new Path("output/" + Cluster.CLUSTERED_POINTS_DIR
                        + "/part-m-00000"), configuration);
        IntWritable key = new IntWritable();
        WeightedVectorWritable value = new WeightedVectorWritable();
        while (reader.next(key, value)) {
            System.out.println(value.toString() + " belongs to cluster "
                    + key.toString());
        }
        reader.close();

    }



}
