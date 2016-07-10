package com.rubyhuntersky.gx.puddles.welders

import com.rubyhuntersky.gx.basics.Frame

/**
 * @author Jeffrey Yu
 * @since 7/9/16.
 */

interface RandomAccessTwoFrameWelder {

    interface Listener {
        fun onWeld(weld: TwoFrameWeld)
    }

    var firstFrame: Frame?
    var secondFrame: Frame?
    var listener: Listener?

    companion object {
        fun create(onFrames: (firstFrame: Frame, secondFrame: Frame) -> TwoFrameWeld): RandomAccessTwoFrameWelder {
            return object : RandomAccessTwoFrameWelder {
                override var firstFrame: Frame? = null
                    set(value) {
                        field = value
                        weld()
                    }
                override var secondFrame: Frame? = null
                    set(value) {
                        field = value
                        weld()
                    }
                override var listener: Listener? = null
                    set(value) {
                        field = value
                        weld()
                    }

                private fun weld() {
                    if (firstFrame == null || secondFrame == null || listener == null) return
                    val weld = onFrames(firstFrame!!, secondFrame!!)
                    listener!!.onWeld(weld)
                }
            }
        }
    }
}