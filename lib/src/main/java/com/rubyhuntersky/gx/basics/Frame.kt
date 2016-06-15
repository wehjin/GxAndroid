package com.rubyhuntersky.gx.basics

/**
 * @author wehjin
 * *
 * @since 1/23/16.
 */

data class Frame(val horizontal: Range, val vertical: Range, val elevation: Int) {

    constructor(width: Float, height: Float, elevation: Int) : this(Range.of(0f, width), Range.of(0f, height), elevation)

    val mid: Spot
        get() = Spot(horizontal.mid, vertical.mid, elevation.toFloat())

    fun withVerticalShift(shift: Float): Frame {
        val newVertical = vertical.shift(shift)
        if (newVertical === vertical) {
            return this
        }
        return Frame(horizontal, newVertical, elevation)
    }

    fun withShift(horizontalShift: Float, verticalShift: Float): Frame {
        val newVertical = vertical.shift(verticalShift)
        val newHorizontal = horizontal.shift(horizontalShift)
        if (newVertical === vertical && newHorizontal === horizontal) {
            return this
        }
        return Frame(newHorizontal, newVertical, elevation)
    }

    fun withVertical(range: Range): Frame {
        return Frame(horizontal, range, elevation)
    }

    fun withVerticalLength(verticalLength: Float): Frame {
        val newVertical = vertical.withLength(verticalLength)
        return Frame(horizontal, newVertical, elevation)
    }
}
