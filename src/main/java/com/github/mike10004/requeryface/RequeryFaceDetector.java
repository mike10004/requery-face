package com.github.mike10004.requeryface;

import java.util.List;
import java.util.stream.Collectors;

public class RequeryFaceDetector {

    public List<Detection> detect(Cascade cascade, Canvas<?> canvas, DetectionOptions options) {
        ObjectDetector detector = createObjectDetector(cascade, canvas, options);
        Canvas<?>[] canvasArray = detector.prepare();
        List<Detection> detections = detector.analyze(canvasArray, cascade);
        detections = detector.clean(detections);
        detections = detections.stream().filter(face -> face.confidence >= options.confidence).collect(Collectors.toList());
        return detections;
    }

    protected ObjectDetector createObjectDetector(Cascade cascade, Canvas canvas, DetectionOptions options) {
        return new ObjectDetector(canvas, cascade, options.interval, options.min_neighbors);
    }
}
