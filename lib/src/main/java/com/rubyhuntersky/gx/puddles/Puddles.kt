package com.rubyhuntersky.gx.puddles

import com.rubyhuntersky.gx.basics.Frame
import com.rubyhuntersky.gx.reactions.Reaction
import java.util.*

/**
 * @author Jeffrey Yu
 * @since 7/9/16.
 */

object Puddles {

    private val idGenerator = Random()

    fun colorPuddle(width: Float, height: Float, color: Int): Puddle {
        return object : Puddle {
            override fun present(viewer: Puddle.Viewer, director: Puddle.Director): Puddle.Presentation {
                val id = idGenerator.nextLong()
                val position = Frame(width, height, 0)
                viewer.addPatch(id, position, color)
                return object : Puddle.Presentation {
                    override fun end() {
                        viewer.removePatch(id)
                    }
                }
            }
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
}