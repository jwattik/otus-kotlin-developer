package ru.otus

import kotlin.math.pow

class Square(val x: Int): Figure {
    override fun area(): Int = x.toDouble().pow(2.0).toInt()

    override fun equals(other: Any?): Boolean {
        if (other?.javaClass != javaClass) {
            return false
        }

        other as Square

        return x == other.x
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}