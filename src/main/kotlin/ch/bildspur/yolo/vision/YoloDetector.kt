package ch.bildspur.yolo.vision

import org.opencv.core.Core
import org.opencv.core.Mat
import processing.core.PApplet

class YoloDetector : ImageDetector{

    override fun setup(parent: PApplet) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    }

    override fun detect(image: Mat): ImageDetectionResult {
        return ImageDetectionResult()
    }
}