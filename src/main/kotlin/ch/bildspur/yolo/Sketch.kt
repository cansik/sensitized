package ch.bildspur.yolo

import ch.bildspur.yolo.io.ImageSource
import ch.bildspur.yolo.io.SingleImageSource
import ch.bildspur.yolo.vision.ImageDetector
import ch.bildspur.yolo.vision.YoloDetector
import ch.bildspur.yolo.vision.toMat
import processing.core.PApplet


class Sketch : PApplet() {

    val source : ImageSource = SingleImageSource("data/lena.png")
    val detector : ImageDetector = YoloDetector()

    override fun settings() {
        size(512, 512, FX2D)
    }

    override fun setup() {
        source.setup(this)
        detector.setup(this)
    }

    override fun draw() {
        background(55f)

        val image = source.readImage()
        val result = detector.detect(image.toMat())
        image(source.readImage(), 0f, 0f)
    }

    override fun stop() {
    }

    fun run() {
        runSketch()
    }
}