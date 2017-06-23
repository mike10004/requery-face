package com.github.mike10004.requeryface;

import com.google.common.io.Files;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Program that reads a directory of images and writes face-rectangle-annotated versions.
 */
public class Annotate {

    public static void main(String[] args) throws Exception {
        Stroke annotationStroke = new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);
        Color color = Color.green;
        File dir = new File("src/test/resources/flickr-publicdomain");
        File outputDir = new File("target", "annotated");
        Collection<File> files = org.apache.commons.io.FileUtils.listFiles(dir, new String[]{"jpg"}, false);
        for (File file : files) {
            System.out.format("processing %s...", file.getName());
            BufferedImage image = checkNotNull(ImageIO.read(file), "no jpeg image reader");
            RequeryFaceDetector detector = new RequeryFaceDetector();
            Cascade cascade = Cascade.getDefault();
            List<Detection> detections = detector.detect(cascade, BufferedCanvas.from(image), DetectionOptions.getDefault());
            System.out.format("%d face(s) detected%n", detections.size());
            if (!detections.isEmpty()) {
                for (Detection detection : detections) {
                    Graphics2D g = image.createGraphics();
                    g.setStroke(annotationStroke);
                    g.setColor(color);
                    Rectangle face = new Rectangle((int) Math.round(detection.x), (int) Math.round(detection.y), (int) Math.round(detection.width), (int) Math.round(detection.height));
                    g.drawRect(face.x, face.y, face.width, face.height);
                }
                File outfile = new File(outputDir, FilenameUtils.getBaseName(file.getName()) + "-annotated.png");
                Files.createParentDirs(outfile);
                ImageIO.write(image, "png", outfile);
            }
        }
    }
}
