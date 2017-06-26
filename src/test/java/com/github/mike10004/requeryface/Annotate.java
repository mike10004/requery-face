package com.github.mike10004.requeryface;

import com.google.common.io.Files;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Program that reads a directory of images and writes face-rectangle-annotated versions.
 */
public class Annotate {

    public static void main(String[] args) throws Exception {
        File dir = new File("src/test/resources/flickr-publicdomain");
        Collection<File> files = org.apache.commons.io.FileUtils.listFiles(dir, new String[]{"jpg"}, false);
        FaceImageProcessor processor = new FaceImageProcessor();
        processor.detectAll(files, new DetectionListener() {
            private final Stroke annotationStroke = new BasicStroke(3, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);
            private final Color color = Color.green;
            private final File outputDir = new File("target", "annotated");

            @Override
            public void detectingFaces(File imageFile) throws IOException {
                System.out.format("processing %s...", imageFile.getName());
            }

            @Override
            public void facesDetected(File imageFile, BufferedImage image, List<Detection> detections) throws IOException {
                System.out.format("%d face(s) detected%n", detections.size());
                if (!detections.isEmpty()) {
                    for (Detection detection : detections) {
                        Graphics2D g = image.createGraphics();
                        g.setStroke(annotationStroke);
                        g.setColor(color);
                        Rectangle face = new Rectangle((int) Math.round(detection.x), (int) Math.round(detection.y), (int) Math.round(detection.width), (int) Math.round(detection.height));
                        g.drawRect(face.x, face.y, face.width, face.height);
                    }
                    File outfile = new File(outputDir, FilenameUtils.getBaseName(imageFile.getName()) + "-annotated.png");
                    Files.createParentDirs(outfile);
                    ImageIO.write(image, "png", outfile);
                }

            }
        });
    }

    public interface DetectionListener {
        default void detectingFaces(File imageFile) throws IOException {}
        void facesDetected(File imageFile, BufferedImage image, List<Detection> detections) throws IOException;
    }

    public static class FaceImageProcessor {
        public void detectAll(Iterable<File> imageFiles, DetectionListener detectionListener) throws IOException {
            for (File imageFile : imageFiles) {
                detectionListener.detectingFaces(imageFile);
                BufferedImage image = ImageIO.read(imageFile);
                RequeryFaceDetector detector = new RequeryFaceDetector();
                Cascade cascade = Cascade.getDefault();
                List<Detection> detections = detector.detect(cascade, BufferedCanvas.from(image), DetectionOptions.getDefault());
                detectionListener.facesDetected(imageFile, image, detections);
            }

        }
    }
}
