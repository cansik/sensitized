package ch.bildspur.yolo.io

import processing.core.PApplet
import processing.serial.Serial

class SerialRenderer(val serialDevice : String) {
    lateinit var port: Serial

    fun setup(parent : PApplet)
    {
        port = Serial(parent, serialDevice,115200)
    }

    fun render(value : Int)
    {
        port.write("$value\n")
    }
}