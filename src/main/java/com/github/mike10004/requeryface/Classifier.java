package com.github.mike10004.requeryface;

public class Classifier {

    public final int count;
    public Feature[] orig_feature;
    public Feature[] feature;
    public final double[] alpha;
    public final double threshold;

    @SuppressWarnings("unused") // deserialized
    private Classifier() {
        count = 0;
        alpha = null;
        threshold = Double.NaN;
    }

    @SuppressWarnings("unused") // deserialized
    public Classifier(int count, Feature[] feature, double[] alpha, double threshold) {
        this.count = count;
        this.feature = feature;
        this.alpha = alpha;
        this.threshold = threshold;
    }

    /**
     * Saves reference to {@link #feature} as {@link #orig_feature}.
     */
    public void storeFeatures() {
        orig_feature = feature;
    }
}
