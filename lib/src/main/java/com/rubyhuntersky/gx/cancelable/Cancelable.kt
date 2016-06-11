package com.rubyhuntersky.gx.cancelable

/**
 * @author wehjin
 * *
 * @since 1/23/16.
 */

interface Cancelable {
    val isCancelled: Boolean
    fun cancel()
}

abstract class BooleanCancelable() : Cancelable {

    private var cancelled = false

    override val isCancelled: Boolean
        get() = cancelled

    override fun cancel() {
        if (!cancelled) {
            cancelled = true
            onCancel()
        }
    }

    abstract fun onCancel()
}
