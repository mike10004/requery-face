package com.github.mike10004.requeryface;

import java.util.List;
import java.util.stream.Collectors;

public class RequeryFaceDetector {

    static class Options {
        public int interval = 4;
        public int min_neighbors = 1;
        public double confidence = 0;
    }

    public List<Detection> analyze(Canvas canvas, Cascade cascade, Options options) {
        ObjectDetector detector = new ObjectDetector(canvas, cascade, options.interval, options.min_neighbors);
        Canvas[] canvasArray = detector.pre();
        List<Detection> detections = detector.core(canvasArray);
        detections = detector.post(detections);
        detections = detections.stream().filter(face -> face.confidence >= options.confidence).collect(Collectors.toList());
        return detections;
    }

}
