package com.github.mike10004.requeryface;

import org.junit.Test;

import static org.junit.Assert.*;

public class DetectionTest {

    @Test
    public void fuzzyEquals() throws Exception {
        Detection d1 = new Detection(100, 120, 300, 350, 1, 0.5);
        Detection d2 = new Detection(100, 120, 300, 350, 1, 0.5);
        assertTrue("exact", d1.equals(d2));
        d2.x = 101;
        assertTrue("x delta <= 1", d1.fuzzyEquals(d2, 1.5));
    }

}