package com.github.mike10004.requeryface;

import com.github.mike10004.requeryface.Annotate.FaceImageProcessor;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.io.File;
import java.io.Reader;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class CorrectnessRegressionTest {

    @Test
    public void confirmKnownFaceDetectionResults() throws Exception {
        File dir = new File("target/test-classes/flickr-publicdomain");
        URL referenceResource = getClass().getResource("/correctness-reference.json");
        CharSource referenceSource = Resources.asCharSource(referenceResource, UTF_8);
        Map<String, Detection[]> reference;
        try (Reader reader = referenceSource.openStream()) {
            reference = new Gson().fromJson(reader, new TypeToken<Map<String, Detection[]>>() {}.getType());
        }
        Collection<File> files = reference.keySet().stream().map(filename -> new File(dir, filename)).collect(Collectors.toList());
        FaceImageProcessor imageProcessor = new FaceImageProcessor();
        AtomicInteger numDeviations = new AtomicInteger(0);
        double tolerance = 0.5;
        imageProcessor.detectAll(files, (imageFile, image, detections) -> {
            for (int i = 0; i < detections.size(); i++) {
                Detection d = detections.get(i);
                System.out.format("%6.1f %6.1f %6.1f %6.1f %6.4f %d %s%n", d.x, d.y, d.width, d.height, d.confidence, i, imageFile.getName());
            }
            Detection[] expecteds = reference.get(imageFile.getName());
            for (Detection expected : expecteds) {
                if (!containsSimilar(expected, detections, tolerance)) {
                    System.out.format("expected %s missing from actual results %s%n", expected, detections);
                    numDeviations.incrementAndGet();
                }
            }
            if (detections.size() > expecteds.length) { // in case we pick up spurious faces
                numDeviations.incrementAndGet();
            }
        });
        assertEquals("num deviations", 0, numDeviations.get());

    }

    private static boolean containsSimilar(Detection needle, Iterable<Detection> haystack, double tolerance) {
        for (Detection possible : haystack) {
            if (needle.fuzzyEquals(possible, tolerance)) {
                return true;
            }
        }
        return false;
    }
}
