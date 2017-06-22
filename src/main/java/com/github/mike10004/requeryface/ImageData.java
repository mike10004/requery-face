package com.github.mike10004.requeryface;

abstract class ImageData {

    public final int width, height;

    public ImageData(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public abstract int[] toIntArray();
}
