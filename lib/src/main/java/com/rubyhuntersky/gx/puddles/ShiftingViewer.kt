package com.rubyhuntersky.gx.puddles

import com.rubyhuntersky.gx.Human
import com.rubyhuntersky.gx.basics.*
import com.rubyhuntersky.gx.internal.PatchSpecification
import com.rubyhuntersky.gx.internal.shapes.Shape

class ShiftingViewer(val viewer: Puddle.Viewer, initShift: Shift? = null) : Puddle.Viewer {

    private val patches by lazy { mutableMapOf<Long, PatchSpecification>() }
    override val universe: Space get() = viewer.universe
    override val human: Human get() = viewer.human
    var shift: Shift? = initShift
        set(value: Shift?) {
            field = value
            if (value == null) {
                patches.forEach { removeShiftedPatch(it.key) }
            } else {
                patches.forEach { addShiftedPatch(it.key, it.value) }
            }
        }

    override fun getTextSize(text: String, style: TextStyle): TextSize {
        return viewer.getTextSize(text, style)
    }

    override fun addPatch(id: Long, position: Frame, color: Int, shape: Shape) {
        patches[id] = PatchSpecification(position, color, shape)
        if (shift != null) {
            addShiftedPatch(id, patches[id]!!)
        }
    }

    override fun removePatch(id: Long) {
        if (shift != null) {
            removeShiftedPatch(id)
        }
        patches.remove(id)
    }

    private fun addShiftedPatch(id: Long, spec: PatchSpecification) {
        val shiftedPosition = spec.position.withShift(shift!!.horizontal, shift!!.vertical)
        viewer.addPatch(id, shiftedPosition, spec.color, spec.shape)
    }

    private fun removeShiftedPatch(id: Long) {
        viewer.removePatch(id)
    }

}