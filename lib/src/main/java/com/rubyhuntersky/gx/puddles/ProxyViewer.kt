package com.rubyhuntersky.gx.puddles

import com.rubyhuntersky.gx.Human
import com.rubyhuntersky.gx.basics.Frame
import com.rubyhuntersky.gx.basics.Space
import com.rubyhuntersky.gx.basics.TextSize
import com.rubyhuntersky.gx.basics.TextStyle
import com.rubyhuntersky.gx.internal.shapes.Shape

/**
 * @author Jeffrey Yu
 * @since 7/9/16.
 */

open class ProxyViewer(val viewer: Puddle.Viewer) : Puddle.Viewer {
    override val universe: Space get() = viewer.universe
    override val human: Human get() = viewer.human

    override fun getTextSize(text: String, style: TextStyle): TextSize {
        return viewer.getTextSize(text, style)
    }

    override fun addPatch(id: Long, position: Frame, color: Int, shape: Shape) {
        viewer.addPatch(id, position, color, shape)
    }

    override fun removePatch(id: Long) {
        viewer.removePatch(id)
    }
}