package com.rubyhuntersky.gx.reactions

/**
 * @author wehjin
 * *
 * @since 1/27/16.
 */

data class ItemSelectionReaction<T>(override var source: String, val item: T?) : Reaction
