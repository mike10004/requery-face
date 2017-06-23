package com.github.mike10004.requeryface;

import com.github.mike10004.requeryface.RequeryFaceDetector.Options;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ObjectDetectorTest {

    @Test
    public void pre() throws Exception {
        BufferedImage image = Tests.readImageResource("/gwb1.jpg");
        Canvas canvas = BufferedCanvas.from(image);
        Cascade cascade = Cascade.getDefault();
        Options options = new Options();
        ObjectDetector detector = new ObjectDetector(canvas, cascade, options.interval, options.min_neighbors);
        Canvas[] canvasArray = detector.pre();
        for (int i = 0; i < canvasArray.length; i++) {
            Canvas c = canvasArray[i];
            if (c != null) {
                System.out.format("pre ret[%d] = %s%n", i, Arrays.toString(Arrays.copyOf(c.getData(), 8)));
            } else {
                System.out.format("pre ret[%d] = null%n", i);
            }
        }
    }

}