package com.github.mike10004.requeryface;

import org.junit.Test;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class ObjectDetectorTest {

    @Test
    public void analyze() throws Exception {
        BufferedImage image = Tests.readImageResource("/gwb1.jpg");
        Canvas<?> canvas = BufferedCanvas.from(image);
        Cascade cascade = Cascade.getDefault();
        DetectionOptions options = DetectionOptions.getDefault();
        AtomicReference<Canvas[]> canvasArrayHolder = new AtomicReference<>();
        try {
            new ObjectDetector(options.interval, options.min_neighbors) {
                @Override
                protected <T> Canvas<T>[] prepare(Canvas<T> canvas, int scale_upto, int next) {
                    Canvas<T>[] canvasArray = super.prepare(canvas, scale_upto, next);
                    canvasArrayHolder.set(canvasArray);
                    throw new Abort();
                }
            }.analyze(canvas, cascade);
        } catch (Abort /* retry */ ignore) {
        }
        Canvas<?>[] canvasArray = canvasArrayHolder.get();
        for (int i = 0; i < canvasArray.length; i++) {
            Canvas c = canvasArray[i];
            if (c != null) {
                System.out.format("pre ret[%d] = %s%n", i, Arrays.toString(Arrays.copyOf(c.getRgbaData(), 8)));
            } else {
                System.out.format("pre ret[%d] = null%n", i);
            }
        }
    }

    private static class Abort extends Error {

    }
}