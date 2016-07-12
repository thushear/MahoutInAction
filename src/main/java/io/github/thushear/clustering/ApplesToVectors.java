package io.github.thushear.clustering;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.VectorWritable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kongming on 2016/7/12.
 */
public class ApplesToVectors {


    public static void main(String[] args) throws IOException {

        List<NamedVector> apples = new ArrayList<NamedVector>();

        NamedVector apple;
        apple = new NamedVector(
                new DenseVector(new double[] {0.11, 510, 1}),
                "Small round green apple");
        apples.add(apple);
        apple = new NamedVector(
                new DenseVector(new double[] {0.23, 650, 3}),
                "Large oval red apple");
        apples.add(apple);
        apple = new NamedVector(
                new DenseVector(new double[] {0.09, 630, 1}),
                "Small elongated red apple");
        apples.add(apple);
        apple = new NamedVector(
                new DenseVector(new double[] {0.25, 590, 3}),
                "Large round yellow apple");
        apples.add(apple);
        apple = new NamedVector(
                new DenseVector(new double[] {0.18, 520, 2}),
                "Medium oval green apple");

        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(configuration);
        Path path = new Path("appledata/apples");
        SequenceFile.Writer writer = new SequenceFile.Writer(fs, configuration,
                path, Text.class, VectorWritable.class);
        VectorWritable vec = new VectorWritable();
        for (NamedVector vector : apples) {
            vec.set(vector);
            writer.append(new Text(vector.getName()), vec);
        }

        writer.close();
        SequenceFile.Reader reader = new SequenceFile.Reader(fs,
                new Path("appledata/apples"), configuration);
        Text key = new Text();
        VectorWritable value = new VectorWritable();
        while (reader.next(key, value)) {
            System.out.println(key.toString() + " " + value.get().asFormatString());
        }
        reader.close();

    }












}
