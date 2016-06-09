package com.rubyhuntersky.gx.devices.poles

import com.rubyhuntersky.gx.internal.devices.FixedDimensionDevice
import com.rubyhuntersky.gx.internal.screen.Screen
import com.rubyhuntersky.gx.internal.screen.ScreenChain

/**
 * @author wehjin
 * *
 * @since 1/23/16.
 */

open class Pole(val fixedWidth: Float, val relatedHeight: Float, val elevation: Int, screen: Screen?) : ScreenChain(screen), FixedDimensionDevice<Pole> {

    protected constructor(original: Pole) : this(original.fixedWidth, original.relatedHeight, original.elevation, original)

    fun withFixedWidth(fixedWidth: Float): Pole = Pole(fixedWidth, relatedHeight, elevation, this)
    fun withRelatedHeight(relatedHeight: Float): Pole = if (relatedHeight == this.relatedHeight) this else Pole(fixedWidth, relatedHeight, elevation, this)
    fun withShift(horizontal: Float, vertical: Float): Pole = withShift().doShift(horizontal, vertical)
    override fun withShift(): ShiftPole = ShiftPole(this)
    override fun withDelay(): DelayPole = DelayPole(this)
    override fun withElevation(elevation: Int): Pole = if (elevation == this.elevation) this else Pole(fixedWidth, relatedHeight, elevation, this)
    override fun withFixedDimension(fixedDimension: Float): Pole = withFixedWidth(fixedDimension)
    override fun toType(): Pole = this
}
