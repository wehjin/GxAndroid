package com.rubyhuntersky.gx.internal.surface

import com.rubyhuntersky.gx.basics.Spot

/**
 * @author Jeffrey Yu
 * @since 6/7/16.
 */

interface Jester {

    fun getDownReaction(spot: Spot): Reaction
    fun doDown(spot: Spot): Contact;

    interface Contact {

        fun doCancel()

        fun getMoveReaction(spot: Spot): Reaction
        fun doMove(spot: Spot): Contact

        fun getUpReaction(spot: Spot): Reaction
        fun doUp(spot: Spot)
    }

    enum class Reaction {
        WATCH,
        JOIN,
        EXIT,
        WIN
    }
}