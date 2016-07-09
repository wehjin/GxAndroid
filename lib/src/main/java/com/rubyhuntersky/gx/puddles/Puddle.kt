package com.rubyhuntersky.gx.puddles

import com.rubyhuntersky.gx.Human
import com.rubyhuntersky.gx.basics.*
import com.rubyhuntersky.gx.internal.shapes.Shape
import com.rubyhuntersky.gx.reactions.Reaction
import com.rubyhuntersky.gx.uis.Ui

/**
 * @author Jeffrey Yu
 * @since 7/9/16.
 */

interface Puddle {

    interface Viewer : Ui.Viewer {
        override val universe: Space
        override val human: Human
        fun getTextSize(text: String, style: TextStyle): TextSize
        fun addPatch(id: Long, position: Frame, color: Int, shape: Shape)
        fun removePatch(id: Long)
    }

    interface Director : Ui.Director {
        fun onPosition(position: Frame)
        override fun onReaction(reaction: Reaction)
        override fun onError(throwable: Throwable)
    }

    interface Assistant : Ui.Assistant

    interface Presentation {
        fun end()
    }

    companion object {
        fun create(onPresent: (viewer: Puddle.Viewer, director: Puddle.Director) -> Puddle.Presentation): Puddle {
            return object : Puddle {
                override fun present(viewer: Puddle.Viewer, director: Puddle.Director): Puddle.Presentation {
                    try {
                        return onPresent(viewer, director)
                    } catch (throwable: Throwable) {
                        director.onError(throwable)
                        return EMPTY_PRESENTATION
                    }
                }
            }
        }
    }

    fun present(viewer: Viewer, director: Director): Presentation

    fun padOut(padding: Float): Puddle {
        return create({ viewer, director ->
            val shiftingViewer = ShiftingViewer(viewer, Shift(padding, padding, 0f))
            this@Puddle.present(shiftingViewer, object : ProxyDirector(director) {
                override fun onPosition(position: Frame) {
                    super.onPosition(position.withOutsetAndShift(padding))
                }
            })
        })
    }
}