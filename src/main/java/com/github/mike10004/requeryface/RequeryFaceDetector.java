package com.github.mike10004.requeryface;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class RequeryFaceDetector {

    private final Supplier<? extends Cascade> cascadeFactory;

    public RequeryFaceDetector() {
        this(Cascade::getDefault);
    }

    public RequeryFaceDetector(Supplier<? extends Cascade> cascadeFactory) {
        this.cascadeFactory = checkNotNull(cascadeFactory);
    }

    public List<Detection> detect(Canvas<?> canvas, DetectionOptions options) {
        Cascade cascade = cascadeFactory.get();
        ObjectDetector detector = createObjectDetector(cascade, canvas, options);
        List<Detection> detections = detector.analyze(canvas, cascade);
        detections = detections.stream().filter(face -> face.confidence >= options.confidence).collect(Collectors.toList());
        return detections;
    }

    protected ObjectDetector createObjectDetector(Cascade cascade, Canvas canvas, DetectionOptions options) {
        return new ObjectDetector(options.interval, options.min_neighbors);
    }
}
