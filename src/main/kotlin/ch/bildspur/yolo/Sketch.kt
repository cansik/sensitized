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
import processing.core.PConstants
import processing.core.PFont
import kotlin.math.roundToInt


class Sketch : PApplet() {
    private val maxPerson = 10
    private val maxBrightness = 512

    private val source : ImageSource = PS3CamSource() //SingleImageSource("data/lena.png")
    private val detector : ImageDetector = YoloDetector()
    private val renderer = SerialRenderer("/dev/tty.SLAB_USBtoUART")

    var minConfidence = 0.5
    var fpsAverage = ExponentialMovingAverage(0.1)

    val scale = 2f

    lateinit var font : PFont

    override fun settings() {
        size(800, 600, FX2D)
        //fullScreen(PConstants.FX2D)
    }

    override fun setup() {
        source.setup(this)
        detector.setup(this)

        // serial renderer
        renderer.setup(this)
        renderer.startAsync()

        renderer.brightness.value = 0f

        font = createFont("Helvetica", 20f)
        textFont(font, 18f)
    }

    override fun draw() {
        background(22f)

        noCursor()

        val image = source.readImage()

        pushMatrix()
        translate((width / 2)- (image.width * scale / 2f), (height / 2)- (image.height * scale / 2f))
        image(source.readImage(), 0f, 0f,image.width * scale, image.height * scale)
        val result = detector.detect(image.toMat())

        // draw results
        var personCount = 0
        result.detections.filter { it.confidence > minConfidence }
                .forEach{
                    val isPerson = it.name.equals("person") && it.row[0, 5][0] > 0.0

                    noFill()

                    if(isPerson)
                        stroke(0f, 255f, 255f)
                    else
                        stroke(255f, 255f, 0f)

                    strokeWeight(2f)
                    rect(it.x.toFloat() * scale, it.y.toFloat() * scale, it.width.toFloat() * image.width * scale, it.height.toFloat() * image.height * scale)

                    textSize(14f)

                    if(isPerson)
                        fill(0f, 255f, 255f)
                    else
                        fill(255f, 255f, 0f)

                    text("${it.name.toUpperCase()} (${it.confidence.format(2)})", it.x.toFloat() * scale, it.y.toFloat() * scale - 10)

                    if(isPerson)
                        personCount++
                }


        textSize(24f)
        fill(255f)
        text("SENSITIZED", 0f, -20f)

        popMatrix()

        // do something with results
        renderer.brightness.target = map(personCount.toFloat(), 0f, maxPerson.toFloat(), 0f, maxBrightness.toFloat())

        // show fps
        fpsAverage += frameRate.toDouble()
        surface.setTitle("YOLO | FPS: ${frameRate.format(2)}\tAVG: ${fpsAverage.average.format(2)}\tBright: ${renderer.brightness.value.format(2)}")
    }

    override fun stop() {
    }

    fun run() {
        runSketch()
    }
}