package com.rubyhuntersky.gx.puddles

import com.rubyhuntersky.gx.basics.Frame
import com.rubyhuntersky.gx.reactions.Reaction

/**
 * @author Jeffrey Yu
 * @since 7/9/16.
 */

open class ProxyDirector(val director: Puddle.Director) : Puddle.Director {
    override fun onPosition(position: Frame) {
        director.onPosition(position)
    }

    override fun onReaction(reaction: Reaction) {
        director.onReaction(reaction)
    }

    override fun onError(throwable: Throwable) {
        director.onError(throwable)
    }
}