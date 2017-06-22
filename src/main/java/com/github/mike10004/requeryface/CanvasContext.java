package com.github.mike10004.requeryface;

import java.awt.image.BufferedImage;

interface CanvasContext {
    BufferedImage getImageData(int x, int y, int width, int height);
    void drawImage(Canvas canvas, int x, int y, int width, int height, int destX, int destY, int destW, int destH);
}
