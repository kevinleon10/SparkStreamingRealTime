package com.ci1314.ecci.ucr.ac.cr.mediator;

import com.ci1314.ecci.ucr.ac.cr.dataSet.VideoGetter;
import org.bytedeco.javacv.FrameGrabber;

/**
 * Created by lskev on 18-Nov-17.
 */
public class Controller {
    public static void main(String args[]) throws FrameGrabber.Exception {
        VideoGetter videoGetter = new VideoGetter();
        videoGetter.getVideo();
    }
}
