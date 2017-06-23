package com.github.mike10004.requeryface;

class Classifier {
    public Feature[] orig_feature;
    public Feature[] feature;
    public int count;
    public double threshold;
    public double[] alpha;

    /**
     * Copies all features into orig_features.
     */
    public void storeFeatures() {
//        if (feature != null) {
//            orig_feature = new Feature[feature.length];
//            System.arraycopy(feature, 0, orig_feature, 0, feature.length);
//        } else {
//            orig_feature = null;
//        }
        orig_feature = feature;
    }
}
