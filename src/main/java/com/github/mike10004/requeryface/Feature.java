package com.github.mike10004.requeryface;

public class Feature {

    public final int size;
    public final int[] px, py, pz, nx, ny, nz;

    @SuppressWarnings("unused") // for deserialization
    private Feature() {
        size = 0;
        px = null;
        py = null;
        pz = null;
        nx = null;
        ny = null;
        nz = null;
    }

    public Feature(int size) {
        this.size = size;
        this.px = new int[size];
        this.pz = new int[size];
        this.nx = new int[size];
        this.nz = new int[size];
        this.ny = new int[size];
        this.py = new int[size];
    }

    @Override
    public String toString() {
        return "Feature{" +
                "size=" + size +
                '}';
    }
}
