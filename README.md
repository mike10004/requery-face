# requery-face

This is a pure Java port of [Liu Liu's JavaScript face detection code][liuliu]. 
Currently, it's a pretty quick and dirty job. It is *not* any of these things:

* fast
* robust
* thread-safe
* memory-conserving

But if what you're looking for is Java face detection, and you're not all that
concerned with speed or detecting all faces, then this may be for you. Liu Liu
did fine work in writing C code and JavaScript code for face detection, and 
you should check those out if you're really interested in high performance.

## Usage

For an example of how to use the code, check out the `Annotate` class, found
among the unit tests. It is a program that runs over a directory of JPEG files
and re-writes them with face annotations.

The gist of it is something like this:

    BufferedImage image = ImageIO.read(imageFile);
    RequeryFaceDetector detector = new RequeryFaceDetector();
    Cascade cascade = Cascade.getDefault();
    DetectionOptions options = DetectionOptions.getDefault();
    List<Detection> detections = detector.detect(cascade, BufferedCanvas.from(image), options);

It has never been tested with anything but the default set of classifiers and 
the default detection options. Use at your own risk. 

## To do

Some low-hanging fruit to improve the code:

* avoid creating unnecessary arrays, or reuse them where possible
* make the Cascade object immutable
* be more efficient in creating image data integer arrays (instead of using 
  a lot of Raster `getSample(x, y)` calls)

[liuliu]: http://libccv.org/
