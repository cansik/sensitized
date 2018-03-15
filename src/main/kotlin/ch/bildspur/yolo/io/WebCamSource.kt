package ch.bildspur.yolo.io

import processing.core.PApplet
import processing.core.PImage
import processing.video.Capture

class WebCamSource : ImageSource {
    lateinit var cam : Capture

    override fun setup(parent: PApplet) {
        cam = Capture(parent)
        cam.start()
    }

    override fun readImage(): PImage {
        if (cam.available()) {
            cam.read()
        }
        return cam
    }

}