package com.rubyhuntersky.gx.reactions

/**
 * @author Jeffrey Yu
 * @since 6/8/16.
 */

data class TapReaction<T>(val tag: T, override var source: String, val time: Long) : Reaction

