package com.ci1314.ecci.ucr.ac.cr.dataSet;

import com.ci1314.ecci.ucr.ac.cr.filter.Java2DFrameConverter;
import com.ci1314.ecci.ucr.ac.cr.filter.VideoFilter;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class VideoGetter {

    private int color;
    private CanvasFrame canvas;
    private JPanel buttonPanel;
    private JButton normalButton;
    private JButton redButton;
    private JButton greenButton;
    private JButton blueButton;

    public VideoGetter() {
        this.color = 0;
        this.initCanvas();
    }

    private void initCanvas(){
        this.canvas = new CanvasFrame("Spark Streaming Real Time");
        canvas.addWindowListener(new java.awt.event.WindowAdapter()
        {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e)
            {
                System.exit(0);
            }
        });

        this.buttonPanel = new JPanel();
        this.normalButton = new JButton("No Filter");
        this.redButton = new JButton("Red Filter");
        this.greenButton = new JButton("Green Filter");
        this.blueButton = new JButton("Blue Filter");
        this.buttonPanel.add(normalButton);
        this.buttonPanel.add(redButton);
        this.buttonPanel.add(greenButton);
        this.buttonPanel.add(blueButton);

        this.canvas.add(buttonPanel, BorderLayout.SOUTH);

        this.normalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setColor(0);
            }
        });
        this.redButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setColor(1);
            }
        });
        this.greenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setColor(2);
            }
        });
        this.blueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setColor(3);
            }
        });
    }

    public void getVideo() throws FrameGrabber.Exception {

        VideoFilter videoFilter = new VideoFilter();
        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
        FrameGrabber frameGrabber = new OpenCVFrameGrabber(0); // 1 for next camera
        Frame frame;
        BufferedImage bufferedImage;
        frameGrabber.start();
        frame = frameGrabber.grab();

        while (frame != null) {
            // hacer un if, de que si color==0, entonces no haga ningún cambio
            bufferedImage = java2DFrameConverter.getBufferedImage(frame);
            //List<List> list = this.bufferedImageToLine(bufferedImage);
            //list = videoFilter.applySparkFilter(list, this.color);
            //bufferedImage = lineToBufferedImage(list, bufferedImage);
            bufferedImage = videoFilter.applyFilter(bufferedImage, this.color);
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