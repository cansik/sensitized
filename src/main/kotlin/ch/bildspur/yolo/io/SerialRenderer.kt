package ch.bildspur.yolo.io

import ch.bildspur.yolo.easing.EasingFloat
import processing.core.PApplet
import processing.serial.Serial
import kotlin.concurrent.thread
import kotlin.math.roundToInt

class SerialRenderer(val serialDevice : String) {
    private val frameRate = 1000L / 24

    private lateinit var port: Serial

    val brightness = EasingFloat(0.3f)
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
                render(brightness.value.roundToInt())
                brightness.update()
                Thread.sleep(frameRate)
            }
        }
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