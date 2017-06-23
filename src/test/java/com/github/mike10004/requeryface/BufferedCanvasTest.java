package com.github.mike10004.requeryface;

import com.google.common.io.Files;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.*;

public class BufferedCanvasTest {

    @Test
    public void getByteData() throws Exception {
        BufferedImage image = Tests.readImageResource("/gwb1.jpg");
        BufferedCanvas canvas = BufferedCanvas.from(image);
        int[] data = canvas.getByteData();
        long[] histo = new long[256];
        for (int i = 0; i < data.length; i++) {
            int value = data[i];
            if (value < 0 || value > 255) {
                fail("invalid value " + value + " at index " + i);
            }
            histo[value]++;
        }
        System.out.format("%s%n", Arrays.toString(histo));
    }

    @Test
    public void getRgbaData() throws Exception {
        BufferedImage image = Tests.readImageResource("/gwb1.jpg");
        BufferedCanvas canvas = BufferedCanvas.from(image);

        int[] data = canvas.getRgbaData();
        int numChannels = 3 + 1;
        assertEquals("dimensions", image.getWidth() * image.getHeight() * numChannels, data.length);
        int[] expected = {146, 146, 146, 255, 146, 146, 146, 255};
        int[] actual = Arrays.copyOf(data, expected.length);

        assertArrayEquals("data", expected, actual);
    }

    @Test
    public void grayscale() throws Exception {
        BufferedImage image = Tests.readImageResource("/gwb1.jpg");
        BufferedCanvas canvas = BufferedCanvas.from(image);
        byte[] bytes = canvas.writePng();
        File file = new File("target", "image.png");
        Files.write(bytes, file);
        System.out.format("wrote: %s%n", file);
    }
}