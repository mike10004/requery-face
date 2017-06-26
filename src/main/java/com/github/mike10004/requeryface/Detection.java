package com.github.mike10004.requeryface;

import com.google.common.math.DoubleMath;

/**
 * Class that represents a face detection.
 */
public class Detection {

    /**
     * X-coordinate of the upper left corner of a rectangle describing the face location.
     */
    public final double x;
    /**
     * Y-coordinate of the upper left corner of a rectangle describing the face location.
     */
    public final double y;
    /**
     * Width of the rectangle describing the face location.
     */
    public final double width;
    /**
     * Height of the rectangle describing the face location.
     */
    public final double height;

    /**
     * Number of neighbors accumulated by this location in the detection process.
     */
    public final int neighbors;

    /**
     * Confidence of the classifier that this rectangle describes the location of a face.
     */
    public final double confidence;

    /**
     * Constructs a new instance.
     */
    public Detection(double x, double y, double width, double height, int neighbors, double confidence) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.neighbors = neighbors;
        this.confidence = confidence;
    }

    @Override
    public String toString() {
        return String.format("%.0fx%.0f+%.0f+%.0f (%.4f)", width, height, x, y, confidence);
    }

    @Override
    @SuppressWarnings("SimplifiableIfStatement")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Detection)) return false;

        Detection detection = (Detection) o;

        if (Double.compare(detection.x, x) != 0) return false;
        if (Double.compare(detection.y, y) != 0) return false;
        if (Double.compare(detection.width, width) != 0) return false;
        if (Double.compare(detection.height, height) != 0) return false;
        if (neighbors != detection.neighbors) return false;
        return Double.compare(detection.confidence, confidence) == 0;
    }

    /**
     * Checks whether another detection instance is fuzzily equal to this one.
     * @param detection the other detection
     * @param tolerance tolerance to allow in determining rough equality of floating point field values
     * @return true if this detection is fuzzily equal to another
     */
    @SuppressWarnings("SimplifiableIfStatement")
    public boolean fuzzyEquals(Detection detection, double tolerance) {
        if (equals(detection)) {
            return true;
        }
        if (DoubleMath.fuzzyCompare(detection.x, x, tolerance) != 0) return false;
        if (DoubleMath.fuzzyCompare(detection.y, y, tolerance) != 0) return false;
        if (DoubleMath.fuzzyCompare(detection.width, width, tolerance) != 0) return false;
        if (DoubleMath.fuzzyCompare(detection.height, height, tolerance) != 0) return false;
        if (neighbors != detection.neighbors) return false;
        return DoubleMath.fuzzyCompare(detection.confidence, confidence, tolerance) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(width);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(height);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + neighbors;
        temp = Double.doubleToLongBits(confidence);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
