package com.github.mike10004.requeryface;

import com.github.mike10004.requeryface.RequeryFaceDetector.Options;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.awt.image.BufferedImage;
import java.util.List;

import static org.junit.Assert.*;

public class RequeryFaceDetectorTest {

    @org.junit.Test
    public void analyze() throws Exception {
        RequeryFaceDetector detector = new RequeryFaceDetector();
        BufferedImage image = Tests.readImageResource("/gwb1.jpg");
        Canvas canvas = BufferedCanvas.from(image);
        Cascade cascade = Cascade.getDefault();
        long startTime = System.currentTimeMillis();
        List<Detection> detections = detector.analyze(canvas, cascade, new Options());
        long endTime = System.currentTimeMillis();
        System.out.format("%d detections in %.1f seconds%n", detections.size(), (endTime - startTime) / 1000f);
        assertEquals("num detections", 1, detections.size());
        Detection detection = detections.get(0);
        System.out.format("detected: %s%n", detection);
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