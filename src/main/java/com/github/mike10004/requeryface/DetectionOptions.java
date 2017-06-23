/*
 * (c) 2017 Novetta
 *
 * Created by mike
 */
package com.github.mike10004.requeryface;

public class DetectionOptions {

    public final int interval;
    public final int min_neighbors;
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
