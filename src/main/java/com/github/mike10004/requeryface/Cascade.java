package com.github.mike10004.requeryface;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * Class that represents a cascade classifier.
 */
public class Cascade {

    public final int count;
    public final int width;
    public final int height;
    public final Classifier[] stage_classifier;

    @SuppressWarnings("unused")
    private Cascade() { // for deserialization
        count = 0;
        width = 0;
        height = 0;
        stage_classifier = null;
    }

    public Cascade(int count, int width, int height, Classifier[] stage_classifier) {
        this.count = count;
        this.width = width;
        this.height = height;
        this.stage_classifier = stage_classifier;
    }

    /**
     * Loads the default pre-populated classifier from a resource.
     * The object is mutated during processing, so don't try to reuse it.
     * @return the cascade
     */
    public static Cascade getDefault() {
        try (Reader reader = new InputStreamReader(Cascade.class.getResourceAsStream("/default-cascade.json"), StandardCharsets.UTF_8)) {
            return new Gson().fromJson(reader, Cascade.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Saves each classifer's current features in another field.
     * @see Classifier#storeFeatures()
     */
    public void storeFeaturesInClassifiers() {
        if (stage_classifier != null) {
            for (Classifier aStage_classifier : stage_classifier) {
                aStage_classifier.storeFeatures();
            }
        }
    }

    public static class Feature {

        public final int size;
        public final int[] px, py, pz, nx, ny, nz;

        @SuppressWarnings("unused") // for deserialization
        private Feature() {
            size = 0;
            px = null;
            py = null;
            pz = null;
            nx = null;
            ny = null;
            nz = null;
        }

        public Feature(int size) {
            this.size = size;
            this.px = new int[size];
            this.pz = new int[size];
            this.nx = new int[size];
            this.nz = new int[size];
            this.ny = new int[size];
            this.py = new int[size];
        }

        @Override
        public String toString() {
            return "Feature{" +
                    "size=" + size +
                    '}';
        }
    }

    public static class Classifier {

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
}
