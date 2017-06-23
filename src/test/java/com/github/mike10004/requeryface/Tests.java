package com.github.mike10004.requeryface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;

public class Tests {

    private Tests() {
    }

    @SuppressWarnings("SameParameterValue")
    public static BufferedImage readImageResource(String path) throws IOException {
        try (InputStream in = checkNotNull(Tests.class.getResourceAsStream(path), "not found: %s", path)) {
            return checkNotNull(ImageIO.read(in), "no imagereader for %s", path);
        }
    }
}
