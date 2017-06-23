package com.github.mike10004.requeryface;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

class Cascade {

    public int count;
    public int width;
    public int height;
    public Classifier[] stage_classifier;

    public static Cascade getDefault() {
        try (Reader reader = new InputStreamReader(Cascade.class.getResourceAsStream("/default-cascade.json"), StandardCharsets.UTF_8)) {
            return new Gson().fromJson(reader, Cascade.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final Object lock = new Object();

    private static Cascade defaultInstance;

    public void storeFeaturesInClassifiers() {
        if (stage_classifier != null) {
            for (Classifier aStage_classifier : stage_classifier) {
                aStage_classifier.storeFeatures();
            }
        }
    }
}
