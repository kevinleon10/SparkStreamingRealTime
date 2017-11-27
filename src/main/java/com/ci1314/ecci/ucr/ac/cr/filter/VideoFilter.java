package com.ci1314.ecci.ucr.ac.cr.filter;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import scala.Serializable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class VideoFilter implements Serializable {
    private static SparkConf sparkConf;
    private static JavaSparkContext javaSparkContext;

    public VideoFilter() {
        this.sparkConf = new SparkConf().setAppName("SparkFilter").setMaster("local");
        this.javaSparkContext = new JavaSparkContext(sparkConf);
    }

    public BufferedImage applyFilter(BufferedImage bi, int color) {
        //Recorre las imagenes y obtiene el color de la imagen original y la almacena en el destino
        for (int x = 0; x < bi.getWidth(); x++) {
            for (int y = 0; y < bi.getHeight(); y++) {
                //Obtiene el color
                Color c1 = new Color(bi.getRGB(x, y));
                //Calcula la media de tonalidades
                int red = c1.getRed();
                int green = c1.getGreen();
                int blue = c1.getBlue();

                //Almacena el color en la imagen destino
                if (!property(color, red, green, blue)) {
                    int med = (red + green + blue) / 3;
                    bi.setRGB(x, y, new Color(med, med, med).getRGB());
                }
            }
        }
        return bi;
    }

    public List<List> applySparkFilter(List<List> arrayListList, int color) {

        List<List> finalResult = new ArrayList<>();
        /*
        //rgb
        java.util.List exampleArray = Arrays.asList(3, 1, 2, 0);
        java.util.List exampleArray1 = Arrays.asList(4, 1, 2, 0);
        java.util.List exampleArray2 = Arrays.asList(2, 4, 6, 0);

        java.util.List<java.util.List> = Arrays.asList(exampleArray, exampleArray1, exampleArray2);
        */

       JavaRDD<List> javaRDD = javaSparkContext.parallelize(arrayListList); //originales

        //revisa a que indice le hace el filtro
        int firstExtraColor = 1;
        int secondExtraColor = 2;
        if (color==1){
            firstExtraColor = 0;
        }
        else if(color==2){
            secondExtraColor = 0;
        }

        //Esto es por el compilador, debe ser final
        int finalFirstColor = firstExtraColor;
        int finalSecondColor = secondExtraColor;

        //los RDD's filtrados
        JavaRDD<List> filterRDD = javaRDD.map(new Function<List, List>() {
            @Override
            public List call(List list) throws Exception {
                //Si no es el color del filtro entonces se pone en blanco y negro
                if((Integer)list.get(color)<=(Integer)list.get(finalFirstColor) || (Integer)list.get(color)<=(Integer)list.get(finalSecondColor)){
                    int avg = ((Integer)list.get(0) + (Integer)list.get(1) + (Integer)list.get(2)) / 3;
                    list.set(0, avg);
                    list.set(1, avg);
                    list.set(2, avg);
                    list.set(3, 1);
                    //System.out.println(avg);
                }
                return list;
            }
        });
        //Se devuelve la lista modificada
        return filterRDD.collect();
    }

    private boolean property(int color, int red, int green, int blue) {
        boolean result = false;
        switch (color) {
        case 1:
            if (red > blue && red > green) {
                result = true;
            }
            break;
        case 2:
            if (green > blue && green > red) {
                result = true;
            }
            break;
        case 3:
            if (blue > red && blue > green) {
                result = true;
            }
            break;
        default:
            result = true;
            break;
    }
        return result;
}
}
