package com.github.mike10004.requeryface;

import com.google.common.math.DoubleMath;

public class Detection {

    public final double x, y, width, height;
    public final int neighbors;
    public final double confidence;

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
