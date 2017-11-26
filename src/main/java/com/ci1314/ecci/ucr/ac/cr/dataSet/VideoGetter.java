package com.ci1314.ecci.ucr.ac.cr.dataSet;

import com.ci1314.ecci.ucr.ac.cr.filter.Java2DFrameConverter;
import com.ci1314.ecci.ucr.ac.cr.filter.VideoFilter;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class VideoGetter {

    private int color;

    public VideoGetter() {
        this.color = 3;
    }

    public void getVideo() throws FrameGrabber.Exception {
        CanvasFrame canvas = new CanvasFrame("Spark Streaming Real Time");
        VideoFilter videoFilter = new VideoFilter();
        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas.addWindowListener(new java.awt.event.WindowAdapter()
        {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e)
            {
                System.exit(0);
            }
        });

        FrameGrabber frameGrabber = new OpenCVFrameGrabber(0); // 1 for next camera
        Frame frame;
        BufferedImage bufferedImage;
        frameGrabber.start();
        frame = frameGrabber.grab();

        while (frame != null) {
            bufferedImage = java2DFrameConverter.getBufferedImage(frame);
            List<List> list = this.bufferedImageToLine(bufferedImage);
            list = videoFilter.applySparkFilter(list, this.color);
            bufferedImage = lineToBufferedImage(list, bufferedImage);
            frame = java2DFrameConverter.getFrame(bufferedImage);
            canvas.showImage(frame);
            frame = frameGrabber.grab();
        }
        frameGrabber.close();
    }

    public List<List> bufferedImageToLine(BufferedImage image) {

        List<List> list = new ArrayList<>();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                List RGB = new ArrayList<>();
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

    public BufferedImage lineToBufferedImage(List<List> list, BufferedImage image) {
        int actualPosition = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (!list.get(actualPosition).get(3).equals(0)) {
                    image.setRGB(x, y, new Color((int) list.get(actualPosition).get(0), (int) list.get(actualPosition).get(1), (int) list.get(actualPosition).get(2)).getRGB());
                }
                actualPosition++;
            }
        }
        return image;
    }

    public void setColor(int color) {
        this.color = color;
    }
}