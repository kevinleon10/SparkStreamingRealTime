package com.ci1314.ecci.ucr.ac.cr.dataSet;

import com.ci1314.ecci.ucr.ac.cr.filter.Java2DFrameConverter;
import com.ci1314.ecci.ucr.ac.cr.filter.VideoFilter;
import org.bytedeco.javacv.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;


public class VideoGetter {

    public void getVideo() throws FrameGrabber.Exception {
        CanvasFrame canvas = new CanvasFrame("Video");
        VideoFilter colorFilter = new VideoFilter();
        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

        FrameGrabber g = new OpenCVFrameGrabber(0); // 1 for next camera
        org.bytedeco.javacv.Frame img;
        BufferedImage img2;
        g.start();

        while (true) {

            img = g.grab();

            if (img != null) {
                img2 = java2DFrameConverter.getBufferedImage(img);
                //bufferedImageToLine
                //se manda la vara a spark
                //lineToBufferedImage
                img = java2DFrameConverter.getFrame(img2);
                canvas.showImage(img);
            }
        }
    }

    public java.util.List<java.util.List> bufferedImageToLine(BufferedImage image) {

        java.util.List<java.util.List> list = new ArrayList<java.util.List>();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                java.util.List RGB = new ArrayList<>();
                Color c1 = new Color(image.getRGB(x, y));
                RGB.add(c1.getRed());
                RGB.add(c1.getGreen());
                RGB.add(c1.getBlue());
                RGB.add(0);
                list.add(RGB);
            }
        }
        return list;
    }

    public BufferedImage lineToBufferedImage(java.util.List<java.util.List> list, BufferedImage image) {
        int actualPosition = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (list.get(actualPosition).get(3).equals(1)) {
                    image.setRGB(x, y, new Color((int)list.get(actualPosition).get(0), (int)list.get(actualPosition).get(1), (int)list.get(actualPosition).get(2)).getRGB());
                }
                actualPosition++;
            }
        }
        return image;
    }


}