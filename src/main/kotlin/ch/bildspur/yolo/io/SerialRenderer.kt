package ch.bildspur.yolo.io

import processing.core.PApplet
import processing.serial.Serial
import kotlin.concurrent.thread

class SerialRenderer(val serialDevice : String) {
    private val frameRate = 1000L / 24

    private lateinit var port: Serial

    @Volatile var brightness = 0
    @Volatile private  var running = true

    fun setup(parent : PApplet)
    {
        port = Serial(parent, serialDevice,115200)
    }

    fun startAsync(){
        running = true
        thread {
            while (running)
            {
                render(brightness)
                Thread.sleep(frameRate)
            }
        }.start()
    }

    fun stop()
    {
        running = false
    }

    fun render(value : Int)
    {
        port.write("$value\n")
    }
}