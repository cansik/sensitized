package ch.bildspur.yolo.easing

class EasingFloat(var alpha: Float = 0.1f) : EasingObject {
    var value: Float = 0f
    var target: Float = 0f

    override fun update() {
        value += (target - value) * alpha
    }
}