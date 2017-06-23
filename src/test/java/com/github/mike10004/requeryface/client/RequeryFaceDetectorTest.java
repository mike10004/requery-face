package com.github.mike10004.requeryface.client;

import com.github.mike10004.requeryface.BufferedCanvas;
import com.github.mike10004.requeryface.Canvas;
import com.github.mike10004.requeryface.Cascade;
import com.github.mike10004.requeryface.Detection;
import com.github.mike10004.requeryface.RequeryFaceDetector;
import com.github.mike10004.requeryface.DetectionOptions;
import com.github.mike10004.requeryface.Tests;

import java.awt.image.BufferedImage;
import java.util.List;

import static org.junit.Assert.*;

public class RequeryFaceDetectorTest {

    @org.junit.Test
    public void analyze() throws Exception {
        RequeryFaceDetector detector = new RequeryFaceDetector();
        BufferedImage image = Tests.readImageResource("/gwb1.jpg");
        Cascade cascade = Cascade.getDefault();
        long startTime = System.currentTimeMillis();
        List<Detection> detections = detector.detect(cascade, BufferedCanvas.from(image), DetectionOptions.getDefault());
        long endTime = System.currentTimeMillis();
        System.out.format("%d detections in %.1f seconds%n", detections.size(), (endTime - startTime) / 1000f);
        assertEquals("num detections", 1, detections.size());
        Detection detection = detections.get(0);
        System.out.format("detected: %s%n", detection);
        /*
         * These are the detection results when the JavaScript code is run on the same image.
         * Our results are not the same, but they seem to be within about 5% of the image width.
         */
        double expectedX = 34, expectedY = 41;
        double expectedWidth = 62, expectedHeight = 62;
        double tolerance = defaultProportionOfImageWidth * image.getWidth();
        assertAlmostMatches("x", expectedX, detection.x, tolerance);
        assertAlmostMatches("y", expectedY, detection.y, tolerance);
        assertAlmostMatches("width", expectedWidth, detection.width, tolerance);
        assertAlmostMatches("height", expectedHeight, detection.height, tolerance);
    }

    private static final double defaultProportionOfImageWidth = 0.05;

    private void assertAlmostMatches(String message, double expected, double actual, double tolerance) {
        assertTrue(message + " " + actual + " should be " + expected, Math.abs(expected - actual) <= tolerance);
    }

}