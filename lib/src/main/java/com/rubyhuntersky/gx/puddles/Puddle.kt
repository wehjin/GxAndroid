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

    fun poolRight(rightPuddle: Puddle, anchor: Float): Puddle {
        return create({ viewer, director ->
            val composite = CompositePresentation()
            val leftViewer = ShiftingViewer(viewer)
            val rightViewer = ShiftingViewer(viewer)
            val compositeViewer = object : ProxyViewer(viewer) {
                var leftPosition: Frame? = null
                var rightPosition: Frame? = null

                fun onLeftPosition(position: Frame) {
                    leftPosition = position
                    update()
                }

                fun onRightPosition(position: Frame) {
                    rightPosition = position
                    update()
                }

                fun update() {
                    if (leftPosition == null || rightPosition == null) return
                    val (leftHeight, rightHeight) = Pair(leftPosition!!.height, rightPosition!!.height)
                    val (fullWidth, fullHeight) = Pair(leftPosition!!.width + rightPosition!!.width, Math.max(leftHeight, rightHeight))
                    val (leftMargin, rightMargin) = Pair(fullHeight - leftHeight, fullHeight - rightHeight)
                    val (leftDown, rightDown) = Pair(anchor * leftMargin, anchor * rightMargin)
                    leftViewer.shift = Shift(0f, leftDown, 0f)
                    rightViewer.shift = Shift(leftPosition!!.width, rightDown, 0f)
                    director.onPosition(Frame(fullWidth, fullHeight, 0))
                }
            }
            composite.add(this@Puddle.present(leftViewer, object : ProxyDirector(director) {
                override fun onPosition(position: Frame) {
                    compositeViewer.onLeftPosition(position)
                }
            }))
            composite.add(rightPuddle.present(rightViewer, object : ProxyDirector(director) {
                override fun onPosition(position: Frame) {
                    compositeViewer.onRightPosition(position)
                }
            }))
            composite
        })
    }
}