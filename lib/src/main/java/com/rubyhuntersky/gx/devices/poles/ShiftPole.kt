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

    private val pendingPatches = ArrayList<ShiftPatch>()
    private val pendingSurfaces = ArrayList<ShiftSurface>()
    private var didShift: Boolean = false
    private var verticalShift: Float = 0.toFloat()
    private var horizontalShift: Float = 0.toFloat()

    override fun doShift(horizontal: Float, vertical: Float): ShiftPole {
        if (didShift) {
            return this
        }
        didShift = true
        this.horizontalShift = horizontal
        this.verticalShift = vertical
        val shiftPatches = ArrayList(pendingPatches)
        pendingPatches.clear()
        for (patch in shiftPatches) {
            patch.setShift(horizontal, vertical)
        }
        val surfaces = ArrayList(pendingSurfaces)
        pendingSurfaces.clear()
        for (pendingSurface in surfaces) {
            pendingSurface.setShift(horizontal, vertical)
        }
        return this
    }

    override fun addPatch(frame: Frame, shape: Shape, argbColor: Int): Patch {
        val patch = ShiftPatch(frame, shape, argbColor, basis)
        if (didShift) {
            patch.setShift(horizontalShift, verticalShift)
        } else {
            pendingPatches.add(patch)
        }
        return patch
    }

    override fun addSurface(frame: Frame, jester: Jester): Removable {
        val surface = ShiftSurface(frame, jester, { frame, jester -> super.addSurface(frame, jester) })
        if (didShift) {
            surface.setShift(horizontalShift, verticalShift)
            return surface
        } else {
            pendingSurfaces.add(surface)
            return object : Removable {
                override fun remove() {
                    pendingSurfaces.remove(surface)
                    surface.remove()
                }
            }
        }
    }

    inner class ShiftSurface(val frame: Frame, val jester: Jester, val addSurface: (Frame, Jester) -> Removable) : Removable {

        var shifted: Removable? = null

        fun setShift(horizontal: Float, vertical: Float) {
            shifted ?: realizeShifted(horizontal, vertical)
        }

        override fun remove() {
            shifted?.remove()
        }

        private fun realizeShifted(horizontal: Float, vertical: Float) {
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
            shifted = addSurface(shiftedFrame, shiftedJester)
        }
    };
}
