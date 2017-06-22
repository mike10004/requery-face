package com.github.mike10004.requeryface;

import com.github.mike10004.requeryface.RequeryFaceDetector.Options;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.List;

import static org.junit.Assert.*;

public class RequeryFaceDetectorTest {

    @org.junit.Test
    public void analyze() throws Exception {
        RequeryFaceDetector detector = new RequeryFaceDetector();
        BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/gwb1.jpg"));
        Canvas canvas = BufferedCanvas.from(image);
        Cascade cascade = Cascade.getDefault();
        List<Detection> detections = detector.analyze(canvas, cascade, new Options());
        assertEquals("num detections", 1, detections.size());
    }

}