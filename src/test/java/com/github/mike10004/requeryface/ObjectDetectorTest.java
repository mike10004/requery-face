package com.github.mike10004.requeryface;

import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class ObjectDetectorTest {

    @Test
    public void pre() throws Exception {
        BufferedImage image = Tests.readImageResource("/gwb1.jpg");
        Canvas canvas = BufferedCanvas.from(image);
        Cascade cascade = Cascade.getDefault();
        DetectionOptions options = DetectionOptions.getDefault();
        ObjectDetector detector = new ObjectDetector(canvas, cascade, options.interval, options.min_neighbors);
        Canvas[] canvasArray = detector.prepare();
        for (int i = 0; i < canvasArray.length; i++) {
            Canvas c = canvasArray[i];
            if (c != null) {
                System.out.format("pre ret[%d] = %s%n", i, Arrays.toString(Arrays.copyOf(c.getRgbaData(), 8)));
            } else {
                System.out.format("pre ret[%d] = null%n", i);
            }
        }
    }

}