package com.rubyhuntersky.gx.cancelable

/**
 * @author Jeffrey Yu
 * @since 6/10/16.
 */

object CancelledCancelable : Cancelable {
    override val isCancelled: Boolean
        get() = true

    override fun cancel() {
        // Do nothing
    }
}