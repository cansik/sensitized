package ch.bildspur.yolo.io

import processing.core.PApplet
import processing.core.PImage

class SingleImageSource(val imageFileName : String) : ImageSource {
    private lateinit var image : PImage

    override fun setup(parent : PApplet) {
        image = parent.loadImage(imageFileName)
    }

    override fun readImage(): PImage {
        return image
    }
}