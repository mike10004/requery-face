package com.github.mike10004.requeryface;

public class Feature {
    public int size;
    public int[] px, py, pz, nx, ny, nz;

    @SuppressWarnings("unused") // for deserialization
    private Feature() {
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
}
