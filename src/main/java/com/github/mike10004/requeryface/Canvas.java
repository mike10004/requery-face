package com.github.mike10004.requeryface;

/**
 * Class that represents an image.
 */
@SuppressWarnings("SameParameterValue")
public abstract class Canvas<T> {

    public final int width;
    public final int height;

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the image data as an array of integers. The array is the sequence of image pixel rows, and each row
     * consists of four integers per pixel, in RGBA order.
     * @return the array of integers representing pixel channel values
     */
    public abstract int[] getRgbaData();

    public abstract T getSubimage(int x, int y, int width, int height);
    public abstract void drawImage(Canvas canvas, int x, int y, int width, int height, int destX, int destY, int destW, int destH);
    public abstract Canvas<T> createCanvas(int width, int height);
    public abstract Canvas<T> createCanvas(int width, int height, T imageData);

}
