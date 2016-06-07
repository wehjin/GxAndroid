package com.rubyhuntersky.gx.basics

/**
 * @author Jeffrey Yu
 * @since 6/7/16.
 */

data class Spot(val x: Float, val y: Float, val z: Float) {

    fun intersects(frame: Frame): Boolean = frame.horizontal.contains(x) && frame.vertical.contains(y)

    fun distanceSquared(spot: Spot): Float {
        val dx = spot.x - x
        val dy = spot.y - y
        val dz = spot.z - z
        return dx * dx + dy * dy + dz * dz
    }

    fun shifted(shiftX: Float, shiftY: Float, shiftZ: Float): Spot = Spot(x + shiftX, y + shiftY, z + shiftZ)
}
