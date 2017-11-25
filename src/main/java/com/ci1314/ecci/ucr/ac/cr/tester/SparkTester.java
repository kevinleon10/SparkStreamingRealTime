package com.ci1314.ecci.ucr.ac.cr.tester;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lskev on 18-Nov-17.
 */
public class SparkTester {

    public void sparkTester(){
        int mat[][] = {{1, 2, 3}, {3, 4, 5}, {6, 7, 8}};
        int mat1[][] = {{1, 2, 3}, {3, 4, 5}};
        int mat2[][] = {{1, 2, 3}};
        List<int[][]> list = new ArrayList<int[][]>();
        list.add(mat);
        list.add(mat1);
        list.add(mat2);
        //It Configures Spark
        /*SparkConf sparkConf = new SparkConf().setAppName("SparkTester").setMaster("local"); //run with one thread
        //Connection to a Spark cluster, and can be used to create RDDs, accumulators and broadcast variables on that cluster.
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        JavaRDD<int[][]> javaRDD = javaSparkContext.parallelize(list);
        System.out.println(javaRDD.count());*/


        SparkConf sparkConf = new SparkConf().setAppName("SparkTester").setMaster("local");
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        JavaRDD<Integer> intRDD = javaSparkContext.parallelize(Arrays.asList(1, 2, 3, 4, 50, 61, 72));
        JavaRDD<Integer> filteredRDD = intRDD.filter((x) -> (x > 10 ? false : true));
        JavaRDD<Integer> transformedRDD = filteredRDD.map((x) -> (x * x) );
        int sumTransformed = transformedRDD.reduce( (x, y) -> (x + y) );
        System.out.println(sumTransformed);
    }
}
