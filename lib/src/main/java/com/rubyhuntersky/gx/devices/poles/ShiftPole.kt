package com.rubyhuntersky.gx.devices.poles

import com.rubyhuntersky.gx.basics.Frame
import com.rubyhuntersky.gx.basics.Removable
import com.rubyhuntersky.gx.basics.Spot
import com.rubyhuntersky.gx.internal.devices.ShiftDevice
import com.rubyhuntersky.gx.internal.patches.Patch
import com.rubyhuntersky.gx.internal.patches.ShiftPatch
import com.rubyhuntersky.gx.internal.shapes.Shape
import com.rubyhuntersky.gx.internal.surface.Jester
import com.rubyhuntersky.gx.internal.surface.MoveReaction
import com.rubyhuntersky.gx.internal.surface.UpReaction
import java.util.*

/**
 * @author wehjin
 * *
 * @since 1/24/16.
 */
class ShiftPole(original: Pole) : Pole(original), ShiftDevice<Pole> {

    private val patches = ArrayList<ShiftPatch>()
    private val surfaces = ArrayList<ShiftSurface>()
    private var verticalShift: Float = 0f
    private var horizontalShift: Float = 0f

    override fun addPatch(frame: Frame, shape: Shape, argbColor: Int): Patch {
        val patch = ShiftPatch(frame, shape, argbColor, basis)
        patch.setShift(horizontalShift, verticalShift)
        patches.add(patch)
        return Patch {
            patches.remove(patch)
            patch.remove()
        }
    }

    override fun addSurface(frame: Frame, jester: Jester): Removable {
        val surface = ShiftSurface(frame, jester, { frame, jester -> super.addSurface(frame, jester) })
        surface.setShift(horizontalShift, verticalShift)
        surfaces.add(surface)
        return object : Removable {
            override fun remove() {
                surfaces.remove(surface)
                surface.remove()
            }
        }
    }

    override fun doShift(horizontal: Float, vertical: Float): ShiftPole {
        this.horizontalShift = horizontal
        this.verticalShift = vertical
        patches.forEach { it.setShift(horizontal, vertical) }
        surfaces.forEach { it.setShift(horizontal, vertical) }
        return this
    }

    inner class ShiftSurface(val frame: Frame, val jester: Jester, val addSurface: (Frame, Jester) -> Removable) : Removable {

        var shiftedSurface: Removable? = null
        var removed = false

        fun setShift(horizontal: Float, vertical: Float) {
            if (removed) {
                return;
            }
            shiftedSurface?.remove()
            realizeShiftedSurface(horizontal, vertical)
        }

        override fun remove() {
            if (removed) {
                return
            }
            removed = true
            shiftedSurface?.remove()
        }

        private fun realizeShiftedSurface(horizontal: Float, vertical: Float) {
            val shiftedFrame = frame.withShift(horizontal, vertical)
            val shiftedJester = object : Jester {
                override fun getContact(spot: Spot): Jester.Contact? {
                    val contact = jester.getContact(spot.shifted(-horizontal, -vertical, 0f))
                    return object : Jester.Contact {
                        override fun doCancel() {
                            contact!!.doCancel()
                        }

                        override fun getMoveReaction(spot: Spot): MoveReaction {
                            return contact!!.getMoveReaction(spot.shifted(-horizontal, -vertical, 0f))
                        }

                        override fun doMove(spot: Spot): Jester.Contact {
                            return contact!!.doMove(spot.shifted(-horizontal, -vertical, 0f))
                        }

                        override fun getUpReaction(spot: Spot): UpReaction {
                            return contact!!.getUpReaction(spot.shifted(-horizontal, -vertical, 0f))
                        }

                        override fun doUp(spot: Spot) {
                            return contact!!.doUp(spot.shifted(-horizontal, -vertical, 0f))
                        }
                    }
                }
            }
            shiftedSurface = addSurface(shiftedFrame, shiftedJester)
        }
    };
}
