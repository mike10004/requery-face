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
}
