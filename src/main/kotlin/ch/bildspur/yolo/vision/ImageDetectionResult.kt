package ch.bildspur.yolo.vision

import org.opencv.core.Mat

class ImageDetectionResult (val data : Mat, val width : Double, val height : Double)
{
    val detections = (0 until data.rows()).map {
        val x = data[it, 0][0]
        val y = data[it, 1][0]
        val w = data[it, 2][0]
        val h = data[it, 3][0]
        val confidence = data[it, 4][0]

        DetectionArea((x - w / 2.0) * width, (y - h / 2.0) * height, w, h, confidence)
    }

    override fun toString(): String {
        val sb = StringBuilder()

        for (r in 0 until data.rows())
        {
            val x = data[r, 0][0].format(2)
            val y = data[r, 1][0].format(2)
            val w = data[r, 2][0].format(2)
            val h = data[r, 3][0].format(2)

            val confidence = data[r, 4][0].format(2)

            sb.append("Row: $r  X: $x Y: $y W: $w H: $h C: $confidence")
            sb.append("\n")
        }

        return sb.toString()
    }
}