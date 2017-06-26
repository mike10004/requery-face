package com.github.mike10004.requeryface;

/**
 * Options that can be used to parameterize a detection effort.
 */
public class DetectionOptions {

    /**
     * Interval parameter for cascade.
     *
     * <p><i>Editor's note: I'm not quite sure about this, but I imagine this refers to </i>"Interval images between the
     * full size image and the half size one. For example, 2 will generate 2 images in between full size image and
     * half size one: image with full size, image with 5/6 size, image with 2/3 size, image with 1/2 size."
     */
    public final int interval;

    /**
     * Minimum number of neighbors to require in Haar cascade.
     * See <a href="https://stackoverflow.com/questions/22249579/opencv-detectmultiscale-minneighbors-parameter">reference</a>.
     */
    public final int min_neighbors;

    /**
     * Confidence threshold to apply when cleaning an initial list of detections.
     */
    public final double confidence;

    public DetectionOptions(int interval, int min_neighbors, double confidence) {
        this.interval = interval;
        this.min_neighbors = min_neighbors;
        this.confidence = confidence;
    }

    private static final DetectionOptions DEFAULT_INSTANCE = new DetectionOptions(4, 1, 0d);

    public static DetectionOptions getDefault() {
        return DEFAULT_INSTANCE;
    }

    @Override
    public String toString() {
        return "DetectionOptions{" +
                "interval=" + interval +
                ", min_neighbors=" + min_neighbors +
                ", confidence=" + confidence +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetectionOptions)) return false;

        DetectionOptions that = (DetectionOptions) o;

        if (interval != that.interval) return false;
        if (min_neighbors != that.min_neighbors) return false;
        return Double.compare(that.confidence, confidence) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = interval;
        result = 31 * result + min_neighbors;
        temp = Double.doubleToLongBits(confidence);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
