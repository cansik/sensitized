package ch.bildspur.yolo

import ch.bildspur.yolo.io.ImageSource
import ch.bildspur.yolo.io.SingleImageSource
import ch.bildspur.yolo.io.WebCamSource
import ch.bildspur.yolo.util.ExponentialMovingAverage
import ch.bildspur.yolo.vision.ImageDetector
import ch.bildspur.yolo.vision.YoloDetector
import ch.bildspur.yolo.vision.format
import ch.bildspur.yolo.vision.toMat
import processing.core.PApplet


class Sketch : PApplet() {
    private val source : ImageSource = WebCamSource() //SingleImageSource("data/lena.png")
    private val detector : ImageDetector = YoloDetector()

    var minConfidence = 0.8

    var fpsAverage = ExponentialMovingAverage(0.1)

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

        // draw results
        result.detections.filter { it.confidence > minConfidence }
                .forEach{
                    noFill()
                    stroke(0f, 255f, 0f)
                    strokeWeight(2f)
                    rect(it.x.toFloat(), it.y.toFloat(), it.width.toFloat() * image.width, it.height.toFloat() * image.height)

                    textSize(14f)
                    fill(0f, 255f, 0f)
                    text("${it.name.toUpperCase()} (${it.confidence.format(2)})", it.x.toFloat(), it.y.toFloat() - 10)
                }

        // show fps
        fpsAverage += frameRate.toDouble()
        surface.setTitle("YOLO | FPS: ${frameRate.format(2)}\tAVG: ${fpsAverage.average.format(2)}")
    }

    override fun stop() {
    }

    fun run() {
        runSketch()
    }
}