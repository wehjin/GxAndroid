package com.rubyhuntersky.gx.uis

import com.rubyhuntersky.gx.Human
import com.rubyhuntersky.gx.basics.Space
import com.rubyhuntersky.gx.reactions.Reaction

/**
 * @author Jeffrey Yu
 * @since 7/9/16.
 */

interface Ui {

    interface Viewer {
        val universe: Space
        val human: Human
    }

    interface Director {
        fun onReaction(reaction: Reaction)
        fun onError(throwable: Throwable)
    }

    interface Assistant

    interface Presentation
}