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
        synchronized (lock) {
            if (defaultInstance == null) {
                try (Reader reader = new InputStreamReader(Cascade.class.getResourceAsStream("/default-cascade.json"), StandardCharsets.UTF_8)) {
                    defaultInstance = new Gson().fromJson(reader, Cascade.class);
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            return defaultInstance;
        }
    }

    private static final Object lock = new Object();

    private static Cascade defaultInstance;
}
