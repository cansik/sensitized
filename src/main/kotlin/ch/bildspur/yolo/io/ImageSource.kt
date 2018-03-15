package ch.bildspur.yolo.io

import processing.core.PApplet
import processing.core.PImage

interface ImageSource {
    fun setup(parent : PApplet)

    fun readImage() : PImage
}