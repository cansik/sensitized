package ch.bildspur.yolo.vision

import org.opencv.core.Mat
import processing.core.PApplet

interface ImageDetector {
    fun setup(parent : PApplet)
    fun detect(image : Mat) : ImageDetectionResult
}