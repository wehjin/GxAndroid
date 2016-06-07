package com.rubyhuntersky.gx.internal.surface

import com.rubyhuntersky.gx.basics.Spot

/**
 * @author Jeffrey Yu
 * @since 6/7/16.
 */

interface Jester {

    fun getContact(spot: Spot): Contact?;

    interface Contact {

        fun doCancel()

        fun getMoveReaction(spot: Spot): MoveReaction
        fun doMove(spot: Spot): Contact

        fun getUpReaction(spot: Spot): UpReaction
        fun doUp(spot: Spot)
    }

}