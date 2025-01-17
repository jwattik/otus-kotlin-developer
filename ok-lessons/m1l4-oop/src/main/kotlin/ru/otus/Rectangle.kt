package ru.otus

class Rectangle(val width: Int, val height: Int): Figure {
    override fun area() = width * height

    override fun equals(other: Any?): Boolean {
        if (other?.javaClass != javaClass) {
            return false
        }

        other as Rectangle

        if (width != other.width) return false
        if (height != other.height) return false

        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString() = "Rectangle(${width}x$height)"
}