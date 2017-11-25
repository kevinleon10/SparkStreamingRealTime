package com.ci1314.ecci.ucr.ac.cr.dataSet;

import com.ci1314.ecci.ucr.ac.cr.filter.Java2DFrameConverter;
import com.ci1314.ecci.ucr.ac.cr.filter.VideoFilter;
import org.bytedeco.javacv.*;


import java.awt.image.BufferedImage;

/**
 * Created by lskev on 18-Nov-17.
 */
public class VideoGetter {

    public void getVideo() throws FrameGrabber.Exception {
        CanvasFrame canvas = new CanvasFrame("Video");
        VideoFilter colorFilter = new VideoFilter();
        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

        FrameGrabber g = new OpenCVFrameGrabber(0); // 1 for next camera
        org.bytedeco.javacv.Frame img;
        BufferedImage img2 ;
        g.start();

        while (true) {

            img = g.grab();

            if (img != null) {
                img2 = java2DFrameConverter.getBufferedImage(img);
                img2 = colorFilter.applyFilter(img2);
                img = java2DFrameConverter.getFrame(img2);
                canvas.showImage(img);
            }
        }
    }


}
