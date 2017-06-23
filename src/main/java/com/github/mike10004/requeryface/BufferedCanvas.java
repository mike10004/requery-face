package com.github.mike10004.requeryface;

import com.google.common.primitives.UnsignedBytes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class BufferedCanvas extends Canvas {

    static final int REQUIRED_TYPE = BufferedImage.TYPE_BYTE_GRAY;

    private final BufferedImage image;
    private final CanvasContext context;

    public BufferedCanvas(int width, int height) {
        this(width, height, new BufferedImage(width, height, REQUIRED_TYPE));
    }

    public BufferedCanvas(int width, int height, BufferedImage image) {
        super(width, height);
        this.image = checkNotNull(image);
        checkArgument(REQUIRED_TYPE == image.getType(), "image type must be BYTE_GRAY");
        context = new MyCanvasContext();
    }

    private class MyCanvasContext implements CanvasContext {
        @Override
        public BufferedImage getImageData(int x, int y, int width, int height) {
            return image.getSubimage(x, y, width, height);
        }

        @Override
        public void drawImage(Canvas canvas, int x, int y, int width, int height, int destX, int destY, int destW, int destH) {
            BufferedCanvas source = (BufferedCanvas) canvas;
            BufferedImage sourceImage = source.image.getSubimage(x, y, width, height);
            Graphics2D g = image.createGraphics();
            double sx = (double) destW / (double) width;
            double sy = (double) destH / (double) height;
            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(sx, sy), AffineTransformOp.TYPE_BICUBIC);
            g.drawImage(sourceImage, op, destX, destY);
        }

    }

    @Override
    public CanvasContext getContext(String type) {
        return context;
    }

    public int[] getByteData() {
        int width = image.getWidth(), height = image.getHeight();
        int[] pixels = new int[width * height];
//        int x = 0, y = 0;
//        PixelGrabber grabber = new PixelGrabber(image, x, y, width, height, pixels, 0, 0);
//        try {
//            grabber.grabPixels();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        checkState(grabber.getPixels() == pixels);
        Raster raster = image.getData();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gray = raster.getSample(x, y, 0);
                pixels[y * width + x] = gray;
            }
        }
//        for (int i = 0; i < pixels.length; i++) {
//            pixels[i] =
//        }
        return pixels;
    }

    /**
     * Gets data as a sequence of RGBA pixel quartets.
     * @return
     */
    @Override
    public int[] getData() {
        int[] pixels = getByteData();
        int[] rgbaPixels = new int[pixels.length * 4];
        Arrays.fill(rgbaPixels, 255);
        for (int i = 0; i < pixels.length; i++) {
            rgbaPixels[i * 4 + 0] = pixels[i];
            rgbaPixels[i * 4 + 1] = pixels[i];
            rgbaPixels[i * 4 + 2] = pixels[i];
        }
        return rgbaPixels;
    }

    private static int clamp(int gray) {
        if (gray > 255) {
            gray = 255;
        }
        if (gray < 0) {
            gray = 0;
        }
        return gray;
    }

    public static BufferedCanvas from(BufferedImage image) {
        int width = image.getWidth(), height = image.getHeight();
        if (image.getType() != REQUIRED_TYPE) {
            BufferedImage grayImage = new BufferedImage(width, height, REQUIRED_TYPE);
//            Graphics2D g = grayImage.createGraphics();
//            g.drawImage(image, identity, 0, 0);
            Raster raster = image.getRaster();
            WritableRaster out = grayImage.getRaster();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int r = raster.getSample(x, y, 0);
                    int g = raster.getSample(x, y, 1);
                    int b = raster.getSample(x, y, 2);
//                    int a = raster.getSample(x, y, 3);
                    int gray = (int) Math.round(0.30 * r + 0.59 * g + 0.11 * b);
                    gray = clamp(gray);
                    out.setSample(x, y, 0, gray);
                }
            }
            return new BufferedCanvas(width, height, grayImage);
        }
        return new BufferedCanvas(width, height, image);
    }

    private static final AffineTransformOp identity = new AffineTransformOp(new AffineTransform(), AffineTransformOp.TYPE_BICUBIC);

    public byte[] writePng() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(10 * 1024);
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }
}
