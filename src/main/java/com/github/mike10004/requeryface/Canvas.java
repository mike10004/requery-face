package com.github.mike10004.requeryface;

abstract class Canvas {

    public final int width;
    public final int height;

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @SuppressWarnings("SameParameterValue")
    public abstract CanvasContext getContext(String type);

    public abstract int[] getData();

}
