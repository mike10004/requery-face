package com.github.mike10004.requeryface;

/**
 * Abstract class that represents an image data container.
 *
 * @param <T> the image data buffer type
 */
@SuppressWarnings("SameParameterValue")
public abstract class Canvas<T> {

    /**
     * Width of the image this canvas represents, in pixels.
     */
    public final int width;

    /**
     * Height of the image this canvas represents, in pixels.
     */
    public final int height;

    protected Canvas(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the image data as an array of integers. The array is the sequence of image pixel rows, and each row
     * consists of four integers per pixel, in RGBA order.
     * @return the array of integers representing pixel channel values
     */
    public abstract int[] getRgbaData();

    /**
     * Gets a reference to a region of this canvas.
     */
    public abstract T getRegion(int x, int y, int width, int height);

    /**
     * Draws a source image onto a particular region of this canvas.
     */
    public abstract void drawRegion(T source, int destX, int destY, int destW, int destH);

    /**
     * Creates a new canvas of with the same data buffer type and the given dimensions.
     */
    public abstract Canvas<T> createCanvas(int width, int height);

    /**
     * Creates a new canvas of with the given data buffer and dimensions.
     */
    public abstract Canvas<T> createCanvas(int width, int height, T imageData);

}
