package com.github.mike10004.requeryface;

import com.google.common.primitives.UnsignedBytes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;

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

    private static class IntImageData extends ImageData {

        private int[] data;

        public IntImageData(int width, int height, int[] data) {
            super(width, height);
            this.data = checkNotNull(data);
        }

        @Override
        public int[] toIntArray() {
            return data;
        }
    }

    private class MyCanvasContext implements CanvasContext {
        @Override
        public BufferedImage getImageData(int x, int y, int width, int height) {
            return image.getSubimage(x, y, width, height);
//            byte[] byteData;
//            if (x == 0 && y == 0 && width == 0 && height == 0) {
//                DataBufferByte buffer = (DataBufferByte) image.getRaster().getDataBuffer();
//                byteData = buffer.getData();
//            } else {
//                int[] pixels = new int[width * height];
//                PixelGrabber grabber = new PixelGrabber(image, x, y, width, height, pixels, 0, 0);
//                try {
//                    grabber.grabPixels();
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                int[] data = (int[]) grabber.getPixels();
//                return new IntImageData(width, height, data);
//            }
//            return new IntImageData(width, height, fromUnsignedBytesToInts(byteData));
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

    static int[] toIntArray(BufferedImage image) {
        int width = image.getWidth(), height = image.getHeight();
        int x = 0, y = 0;
        int[] pixels = new int[width * height];
        PixelGrabber grabber = new PixelGrabber(image, x, y, width, height, pixels, 0, 0);
        try {
            grabber.grabPixels();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        checkState(grabber.getPixels() == pixels);
        return pixels;
    }

    @Override
    public int[] getData() {
        BufferedImage subimage = getContext("2d").getImageData(0, 0, width, height);
        return toIntArray(subimage);
    }

    static int[] fromUnsignedBytesToInts(byte[] data) {
        int[] idata = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            idata[i] = UnsignedBytes.toInt(data[i]);
        }
        return idata;
    }

    public static BufferedCanvas from(BufferedImage image) {
        int width = image.getWidth(), height = image.getHeight();
        if (image.getType() != REQUIRED_TYPE) {
            BufferedImage goodImage = new BufferedImage(width, height, REQUIRED_TYPE);
            Graphics2D g = goodImage.createGraphics();
            g.drawImage(image, identity, 0, 0);
            return new BufferedCanvas(width, height, goodImage);
        }
        return new BufferedCanvas(width, height, image);
    }

    private static final AffineTransformOp identity = new AffineTransformOp(new AffineTransform(), AffineTransformOp.TYPE_BICUBIC);
}
