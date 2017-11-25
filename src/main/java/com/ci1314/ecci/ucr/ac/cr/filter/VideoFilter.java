package com.ci1314.ecci.ucr.ac.cr.filter;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by lskev on 18-Nov-17.
 */
public class VideoFilter {
    private static SparkConf sparkConf;
    private static SparkContext sparkContext;

    public VideoFilter(){
        //this.sparkConf = new SparkConf().setAppName("SparkFilter").setMaster("local");
        //this.sparkContext = new SparkContext(sparkConf);
    }
    public BufferedImage applyFilter(BufferedImage bi) {
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
                if (!property(3, red, green, blue)) {
                    int med = (red + green + blue) / 3;
                    bi.setRGB(x, y, new Color(med, med, med).getRGB());
                }
            }
        }
        return bi;
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
                break;
        }
        return result;
    }
}
