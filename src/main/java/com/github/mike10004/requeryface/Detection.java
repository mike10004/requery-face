package com.github.mike10004.requeryface;

public class Detection {

    public double x, y, width, height;
    public int neighbors;
    public double confidence;

    public Detection() {
        this(0, 0, 0, 0, 0, 0);
    }

    public Detection(double x, double y, double width, double height, int neighbor, double confidence) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.neighbors = neighbor;
        this.confidence = confidence;
    }

    @Override
    public String toString() {
        return String.format("%.0fx%.0f+%.0f+%.0f (%.4f)", width, height, x, y, confidence);
    }
}
