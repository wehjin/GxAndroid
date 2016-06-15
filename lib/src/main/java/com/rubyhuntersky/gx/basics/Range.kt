package com.rubyhuntersky.gx.basics

/**
 * @author wehjin
 * *
 * @since 1/23/16.
 */

data class Range(val start: Float, val end: Float) {

    val mid: Float
        get() = (start + end) / 2

    fun toLength(): Float {
        return end - start
    }

    fun contains(value: Float): Boolean = value >= start && value < end

    fun union(range: Range): Range {
        val newStart = Math.min(start, range.start)
        val newEnd = Math.max(end, range.end)
        if (isEqual(newStart, newEnd)) {
            return this
        } else if (range.isEqual(newStart, newEnd)) {
            return range
        } else {
            return Range(newStart, newEnd)
        }
    }

    fun inset(amount: Float): Range {
        if (amount == 0f) {
            return this
        }
        return Range(start + amount, end - amount)
    }

    fun outset(amount: Float): Range {
        if (amount == 0f) {
            return this
        }
        return Range(start - amount, end + amount)
    }

    fun moveStart(amount: Float): Range {
        if (amount == 0f) {
            return this
        }
        return Range(start + amount, end)
    }

    fun moveEnd(amount: Float): Range {
        if (amount == 0f) {
            return this
        }
        return Range(start, end + amount)
    }

    fun shift(shift: Float): Range {
        if (shift == 0f) {
            return this
        }
        return Range(start + shift, end + shift)
    }

    fun withLength(length: Float): Range {
        return Range(start, start + length)
    }

    private fun isEqual(start: Float, end: Float): Boolean {
        return this.start == start && this.end == end
    }

    companion object {

        val ZERO = Range(0f, 0f)

        fun of(start: Float, end: Float): Range {
            return Range(start, end)
        }
    }
}
