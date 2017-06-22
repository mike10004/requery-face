package com.github.mike10004.requeryface;

class Node {
    public int parent;
    public Detection element;
    public int rank;

    public Node(Detection element) {
        parent = -1;
        this.element = element;
        rank = 0;
    }

    public boolean isElementTruthy() {
        return element != null;
    }
}
