package com.rubyhuntersky.gx.puddles

import com.rubyhuntersky.gx.basics.Frame
import com.rubyhuntersky.gx.basics.TextStyle
import com.rubyhuntersky.gx.internal.shapes.FULL_SHAPE
import com.rubyhuntersky.gx.internal.shapes.TextShape
import com.rubyhuntersky.gx.reactions.Reaction
import java.util.*

/**
 * @author Jeffrey Yu
 * @since 7/9/16.
 */

val ID_GENERATOR = Random()

val EMPTY_PRESENTATION = object : Puddle.Presentation {
    override fun end() {
        // Do nothing
    }
}

val EMPTY_DIRECTOR: Puddle.Director = object : Puddle.Director {
    override fun onPosition(position: Frame) {
        // Do nothing
    }

    override fun onReaction(reaction: Reaction) {
        // Do nothing
    }

    override fun onError(throwable: Throwable) {
        // Do nothing
    }
}

fun createPuddle(onPresent: (viewer: Puddle.Viewer, director: Puddle.Director) -> Puddle.Presentation): Puddle {
    return Puddle.create(onPresent)
}

fun textLinePuddle(text: String, style: TextStyle): Puddle {
    return createPuddle { viewer, director ->
        val id = ID_GENERATOR.nextLong()
        val textSize = viewer.getTextSize(text, style)
        val shape = TextShape(text, style, textSize)
        val position = Frame(textSize.textWidth, textSize.textHeight.height, 0)
        viewer.addPatch(id, position, style.typecolor, shape)
        director.onPosition(position)
        object : Puddle.Presentation {
            override fun end() {
                viewer.removePatch(id)
            }
        }
    }
}

fun colorPuddle(width: Float, height: Float, color: Int): Puddle {
    return createPuddle { viewer, director ->
        val id = ID_GENERATOR.nextLong()
        val position = Frame(width, height, 0)
        viewer.addPatch(id, position, color, FULL_SHAPE)
        director.onPosition(position)
        object : Puddle.Presentation {
            override fun end() {
                viewer.removePatch(id)
            }
        }
    }
}
