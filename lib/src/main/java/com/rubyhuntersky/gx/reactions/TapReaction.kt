package com.rubyhuntersky.gx.reactions

import com.rubyhuntersky.gx.basics.Spot

/**
 * @author Jeffrey Yu
 * @since 6/8/16.
 */

data class TapReaction(override var source: String, val time: Long, val surfaceOffset: Spot) : Reaction

