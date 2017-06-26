package com.github.mike10004.requeryface;

import org.junit.Test;

import static org.junit.Assert.*;

public class DetectionTest {

    @Test
    public void fuzzyEquals() throws Exception {
        Detection d1 = new Detection(100, 120, 300, 350, 1, 0.5);
        Detection d2 = new Detection(d1.x, d1.y, d1.width, d1.height, d1.neighbors, d1.confidence);
        assertTrue("exact", d1.equals(d2));
        d2 = new Detection(d1.x + 0.5, d1.y, d1.width, d1.height, d1.neighbors, d1.confidence);
        assertTrue("x delta < 1", d1.fuzzyEquals(d2, 1.0));
    }

}