package com.rubyhuntersky.gx.puddles

import com.rubyhuntersky.gx.Human
import com.rubyhuntersky.gx.basics.Frame
import com.rubyhuntersky.gx.basics.Space
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
        fun addPatch(id: Long, position: Frame, color: Int)
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

    fun present(viewer: Viewer, director: Director): Presentation
}