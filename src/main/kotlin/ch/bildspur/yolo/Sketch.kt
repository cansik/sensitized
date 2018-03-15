package ch.bildspur.yolo

import ch.bildspur.yolo.easing.EasingFloat
import ch.bildspur.yolo.io.ImageSource
import ch.bildspur.yolo.io.PS3CamSource
import ch.bildspur.yolo.io.SerialRenderer
import ch.bildspur.yolo.util.ExponentialMovingAverage
import ch.bildspur.yolo.vision.ImageDetector
import ch.bildspur.yolo.vision.YoloDetector
import ch.bildspur.yolo.vision.format
import ch.bildspur.yolo.vision.toMat
import processing.core.PApplet


class Sketch : PApplet() {
    private val maxPerson = 10
    private val maxBrightness = 1024

    private val source : ImageSource = PS3CamSource() //SingleImageSource("data/lena.png")
    private val detector : ImageDetector = YoloDetector()
    private val renderer = SerialRenderer("/dev/tty.SLAB_USBtoUART")

    var minConfidence = 0.6
    var fpsAverage = ExponentialMovingAverage(0.1)

    val brightness = EasingFloat(0.3f)

    override fun settings() {
        size(800, 600, FX2D)
    }

    override fun setup() {
        source.setup(this)
        detector.setup(this)
        renderer.setup(this)

        brightness.value = 0f
    }

    override fun draw() {
        background(55f)

        val image = source.readImage()
        val result = detector.detect(image.toMat())
        image(source.readImage(), 0f, 0f)

        // draw results
        var personCount = 0
        result.detections.filter { it.confidence > minConfidence }
                .forEach{
                    noFill()
                    stroke(0f, 255f, 0f)
                    strokeWeight(2f)
                    rect(it.x.toFloat(), it.y.toFloat(), it.width.toFloat() * image.width, it.height.toFloat() * image.height)

                    textSize(14f)
                    fill(0f, 255f, 0f)
                    text("${it.name.toUpperCase()} (${it.confidence.format(2)})", it.x.toFloat(), it.y.toFloat() - 10)

                    if(it.name.equals("person"))
                        personCount++
                }

        // do something with results
        brightness.target = map(personCount.toFloat(), 0f, maxPerson.toFloat(), 0f, maxBrightness.toFloat())
        brightness.update()

        // render
        renderer.render(brightness.value.toInt())

        // show fps
        fpsAverage += frameRate.toDouble()
        surface.setTitle("YOLO | FPS: ${frameRate.format(2)}\tAVG: ${fpsAverage.average.format(2)}\tBright: ${brightness.value.format(2)}")
    }

    override fun stop() {
    }

    fun run() {
        runSketch()
    }
}