package com.github.mike10004.requeryface;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Service class that performs face detection.
 */
public class RequeryFaceDetector {

    private final Supplier<? extends Cascade> cascadeFactory;

    /**
     * Constructs an instance using the default trained classifier.
     * @see Cascade#getDefault()
     */
    public RequeryFaceDetector() {
        this(Cascade::getDefault);
    }

    /**
     * Constructs an instance using a classifier produced by the given factory.
     * @param cascadeFactory the cascade classifier factory
     */
    public RequeryFaceDetector(Supplier<? extends Cascade> cascadeFactory) {
        this.cascadeFactory = checkNotNull(cascadeFactory);
    }

    /**
     * Performs face detection using the default options.
     * @param canvas input image
     * @return list of detections
     */
    public List<Detection> detect(Canvas<?> canvas) {
        return detect(canvas, DetectionOptions.getDefault());
    }

    /**
     * Performs face detection using the given options.
     * @param canvas input image
     * @param options detection options
     * @return list of detections
     */
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
