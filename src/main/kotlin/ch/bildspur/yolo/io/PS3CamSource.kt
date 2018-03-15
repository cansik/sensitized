package ch.bildspur.yolo.io

import com.thomasdiewald.ps3eye.PS3EyeP5
import processing.core.PApplet
import processing.core.PImage

class PS3CamSource : ImageSource {
    lateinit var cam : PS3EyeP5

    override fun setup(parent: PApplet) {
        cam = PS3EyeP5.getDevice(PApplet())!!
        cam.start()
    }

    override fun readImage(): PImage {
        return cam.frame
    }
}