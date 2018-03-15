package ch.bildspur.yolo.vision

import org.opencv.core.Mat

data class DetectionArea (val x: Double,
                          val y: Double,
                          val width : Double,
                          val height : Double,
                          val confidence : Double,
                          val name : String,
                          val row : Mat)