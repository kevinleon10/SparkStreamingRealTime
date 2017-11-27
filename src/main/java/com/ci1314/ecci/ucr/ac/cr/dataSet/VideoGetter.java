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
    private boolean active;
    private CanvasFrame canvas;
    private JPanel buttonPanel;
    private JButton normalButton;
    private JButton redButton;
    private JButton greenButton;
    private JButton blueButton;

    public VideoGetter() {
        this.color = 0;
        this.active = true;
        this.initCanvas();
    }

    private void initCanvas() {
        this.canvas = new CanvasFrame("Spark Streaming Real Time");
        this.canvas.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.canvas.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                String[] options = {"Yes", "No"};
                int option = JOptionPane.showOptionDialog(null, "    ¿Sure you want to exit the application?", "Spark Streaming Real Time", JOptionPane.DEFAULT_OPTION, -1, null, options, options[0]);
                if (option == 0) {
                    e.getWindow().dispose();
                    active = false;
                }
            }
        });

        this.buttonPanel = new JPanel();
        this.normalButton = new JButton("No Filter");
        this.redButton = new JButton("Red Filter");
        this.greenButton = new JButton("Green Filter");
        this.blueButton = new JButton("Blue Filter");
        this.buttonPanel.add(this.normalButton);
        this.buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
        this.buttonPanel.add(this.redButton);
        this.buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
        this.buttonPanel.add(this.greenButton);
        this.buttonPanel.add(Box.createRigidArea(new Dimension(10,0)));
        this.buttonPanel.add(this.blueButton);

        this.canvas.add(this.buttonPanel, BorderLayout.SOUTH);

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

        while (active) {
            frame = frameGrabber.grab();
            if (this.color > 0) {
                bufferedImage = java2DFrameConverter.getBufferedImage(frame);
                //List<List> list = this.bufferedImageToLine(bufferedImage);
                //list = videoFilter.applySparkFilter(list, this.color);
                //bufferedImage = lineToBufferedImage(list, bufferedImage);
                bufferedImage = videoFilter.applyFilter(bufferedImage, this.color);
                frame = java2DFrameConverter.getFrame(bufferedImage);
            }
            canvas.showImage(frame);
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