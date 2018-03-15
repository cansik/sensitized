package ch.bildspur.yolo

import ch.bildspur.yolo.io.ImageSource
import ch.bildspur.yolo.io.SingleImageSource
import processing.core.PApplet


class Sketch : PApplet() {

    val source : ImageSource = SingleImageSource("data/lena.png")

    override fun settings() {
        size(512, 512, FX2D)
    }

    override fun setup() {
        source.setup(this)
    }

    override fun draw() {
        background(55f)

        image(source.readImage(), 0f, 0f)
    }

    override fun stop() {
    }

    fun run() {
        runSketch()
    }
}