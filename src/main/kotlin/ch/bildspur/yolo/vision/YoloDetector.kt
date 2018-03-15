package ch.bildspur.yolo.vision

import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.dnn.Dnn
import org.opencv.dnn.Net
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.COLOR_BGRA2BGR
import processing.core.PApplet
import java.nio.file.Files
import java.nio.file.Paths

class YoloDetector : ImageDetector{
    private val configFile = "data/darknet/yolo.cfg" //"/Users/cansik/Downloads/darknet/cfg/yolo.cfg"
    private val weights = "data/darknet/yolo.weights"
    private val nameFile = "data/darknet/coco.names"

    private lateinit var net : Net
    private lateinit var names : List<String>

    override fun setup(parent: PApplet) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

        // load network
        net = Dnn.readNetFromDarknet(configFile, weights)

        // load names
        names = Files.readAllLines(Paths.get(nameFile))

        if(net.empty())
        {
            println("Can't load network!")
        }
    }

    override fun detect(image: Mat): ImageDetectionResult {
        // convert bgr
        if(image.channels() == 4)
            Imgproc.cvtColor(image, image, COLOR_BGRA2BGR)

        // convert image into batch of images
        val inputBlob = Dnn.blobFromImage(image,
                1 / 255.0,
                Size(416.0, 416.0),
                Scalar(0.0),
                true,
                false)

        // set input
        net.setInput(inputBlob)

        // compute
        return ImageDetectionResult(net.forward("detection_out"),
                names,
                image.cols().toDouble(),
                image.rows().toDouble())
    }
}