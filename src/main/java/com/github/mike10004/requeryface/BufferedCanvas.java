package com.github.mike10004.requeryface;

import com.google.common.primitives.UnsignedBytes;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of a canvas that uses a buffered image to store the underlying image data.
 */
public class BufferedCanvas extends Canvas<BufferedImage> {

    static final int REQUIRED_TYPE = BufferedImage.TYPE_BYTE_GRAY;

    private final BufferedImage image;

    public BufferedCanvas(int width, int height) {
        this(width, height, new BufferedImage(width, height, REQUIRED_TYPE));
    }

    public BufferedCanvas(int width, int height, BufferedImage image) {
        super(width, height);
        this.image = checkNotNull(image);
        checkArgument(REQUIRED_TYPE == image.getType(), "image type must be BYTE_GRAY");
    }

    @Override
    public BufferedImage getRegion(int x, int y, int width, int height) {
        return image.getSubimage(x, y, width, height);
    }

    @Override
    public void drawRegion(BufferedImage sourceImage, int destX, int destY, int destW, int destH) {
        Graphics2D g = image.createGraphics();
        double sx = (double) destW / (double) sourceImage.getWidth();
        double sy = (double) destH / (double) sourceImage.getHeight();
        AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(sx, sy), AffineTransformOp.TYPE_BICUBIC);
        g.drawImage(sourceImage, op, destX, destY);
    }

    /**
     * Gets data as a sequence of RGBA pixel quartets.
     * @return an array of integers representing the image data
     */
    @Override
    public int[] getRgbaData() {
        byte[] byteData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        int[] rgba = new int[byteData.length * 4];
        Arrays.fill(rgba, 255); // alpha channel
        for (int i = 0; i < byteData.length; i++) {
            int value = UnsignedBytes.toInt(byteData[i]);
            for (int ch = 0; ch < 3; ch++) {
                rgba[i * 4 + ch] = value;
            }
        }
        return rgba;
    }

    static BufferedImage toGrayscale(BufferedImage image) {
        int width = image.getWidth(), height = image.getHeight();
        BufferedImage grayImage = new BufferedImage(width, height, REQUIRED_TYPE);
        Graphics2D g = grayImage.createGraphics();
        AffineTransformOp op = new AffineTransformOp(new AffineTransform(), AffineTransformOp.TYPE_BICUBIC);
        g.drawImage(image, op, 0, 0);
        return grayImage;
    }

    public static BufferedCanvas from(BufferedImage image) {
        int width = image.getWidth(), height = image.getHeight();
        if (image.getType() != REQUIRED_TYPE) {
            return new BufferedCanvas(width, height, toGrayscale(image));
        }
        return new BufferedCanvas(width, height, image);
    }

    BufferedImage getImage() throws IOException {
        return image;
    }

    @Override
    public Canvas<BufferedImage> createCanvas(int width, int height) {
        return new BufferedCanvas(width, height);
    }

    @Override
    public Canvas<BufferedImage> createCanvas(int width, int height, BufferedImage imageData) {
        return new BufferedCanvas(width, height, imageData);
    }
}
