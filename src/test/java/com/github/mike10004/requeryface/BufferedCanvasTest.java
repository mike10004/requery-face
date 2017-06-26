package com.github.mike10004.requeryface;

import com.google.common.io.ByteStreams;
import com.google.common.primitives.UnsignedBytes;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BufferedCanvasTest {

    @Test
    public void getRgbaData() throws Exception {
        BufferedImage image = Tests.readImageResource("/gwb1.jpg");
        BufferedCanvas canvas = BufferedCanvas.from(image);
        int[] data = canvas.getRgbaData();
        int numChannels = 3 + 1;
        assertEquals("dimensions", image.getWidth() * image.getHeight() * numChannels, data.length);
        int[] expected = {145, 145, 145, 255, 145, 145, 145, 255};
        int[] actual = Arrays.copyOf(data, expected.length);
        assertArrayEquals("data", expected, actual);
    }

    @Test
    public void toGrayscale() throws Exception {
        byte[] reference = ByteStreams.toByteArray(getClass().getResourceAsStream("/gwb1.intensity.dat"));
        BufferedImage image = BufferedCanvas.toGrayscale(Tests.readImageResource("/gwb1.jpg"));
        byte[] byteData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        int maxDiff = maxDiff(reference, byteData);
        assertTrue("max diff too large", maxDiff <= 1);
    }

    private static int maxDiff(byte[] a, byte[] b) {
        checkArgument(a.length > 0 && a.length == b.length);
        int max = 0;
        for (int i = 0; i < a.length; i++) {
            int diff = Math.abs(UnsignedBytes.toInt(a[i]) - UnsignedBytes.toInt(b[i]));
            if (diff > max) {
                max = diff;
            }
        }
        return max;
    }

}