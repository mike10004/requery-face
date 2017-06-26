package com.github.mike10004.requeryface;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Class that represents a cascade classifier.
 */
public class Cascade {

    public final int count;
    public final int width;
    public final int height;
    private final Classifier[] stage_classifier;

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
     * @return the cascade
     */
    public static Cascade getDefault() {
        try (Reader reader = new InputStreamReader(Cascade.class.getResourceAsStream("/default-cascade.json"), StandardCharsets.UTF_8)) {
            return new Gson().fromJson(reader, Cascade.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public Classifier[] copyStageClassifiers() {
        checkState(stage_classifier != null, "stage_classifier is null");
        return Stream.of(stage_classifier)
                .map(Classifier::copy)
                .toArray(Classifier[]::new);
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
            this(size, new int[size], new int[size], new int[size], new int[size], new int[size], new int[size]);
        }

        private Feature(int size, int[] px, int[] py, int[] pz, int[] nx, int[] ny, int[] nz) {
            this.size = size;
            this.px = checkNotNull(px);
            this.pz = checkNotNull(pz);
            this.nx = checkNotNull(nx);
            this.nz = checkNotNull(nz);
            this.ny = checkNotNull(ny);
            this.py = checkNotNull(py);
        }

        @Override
        public String toString() {
            return "Feature{" +
                    "size=" + size +
                    '}';
        }

        public Feature copy() {
            return new Feature(size, px.clone(), py.clone(), pz.clone(), nx.clone(), ny.clone(), nz.clone());
        }
    }

    public static class Classifier {

        public final int count;
        private final Feature[] feature;
        private final double[] alpha;
        public final double threshold;

        @SuppressWarnings("unused") // deserialized
        private Classifier() {
            count = 0;
            alpha = EMPTY_DOUBLE;
            threshold = Double.NaN;
            feature = EMPTY_FEATURE;
        }

        private static final double[] EMPTY_DOUBLE = new double[0];
        private static final Feature[] EMPTY_FEATURE = new Feature[0];

        @SuppressWarnings("unused") // deserialized
        public Classifier(int count, Feature[] feature, double[] alpha, double threshold) {
            this.count = count;
            this.feature = feature;
            this.alpha = alpha;
            this.threshold = threshold;
        }

        public Feature[] copyFeatures() {
            return Stream.of(feature).map(Feature::copy).toArray(Feature[]::new);
        }

        public Classifier copy() {
            return new Classifier(count, copyFeatures(), alpha, threshold);
        }

        public double getAlpha(int k) {
            return alpha[k];
        }
    }
}
