package com.rubyhuntersky.gx.puddles.presentations

import com.rubyhuntersky.gx.basics.Frame
import com.rubyhuntersky.gx.puddles.ProxyDirector
import com.rubyhuntersky.gx.puddles.Puddle
import com.rubyhuntersky.gx.puddles.ShiftingViewer
import com.rubyhuntersky.gx.puddles.welders.RandomAccessTwoFrameWelder
import com.rubyhuntersky.gx.puddles.welders.TwoFrameWeld

/**
 * @author Jeffrey Yu
 * @since 7/9/16.
 */

abstract class TwoFrameWeldingPresenter(val firstPuddle: Puddle, val secondPuddle: Puddle) {

    abstract fun weldFrames(firstFrame: Frame, secondFrame: Frame): TwoFrameWeld

    fun present(viewer: Puddle.Viewer, director: Puddle.Director): Puddle.Presentation {
        val composite = CompositePresentation()
        val firstViewer = ShiftingViewer(viewer)
        val secondViewer = ShiftingViewer(viewer)
        val welder = RandomAccessTwoFrameWelder.create({ first, second -> weldFrames(first, second) })
        welder.listener = object : RandomAccessTwoFrameWelder.Listener {
            override fun onWeld(weld: TwoFrameWeld) {
                val (firstShift, secondShift, fullFrame) = weld
                firstViewer.shift = firstShift
                secondViewer.shift = secondShift
                director.onPosition(fullFrame)
            }
        }
        composite.add(firstPuddle.present(firstViewer, object : ProxyDirector(director) {
            override fun onPosition(position: Frame) {
                welder.firstFrame = position
            }
        }))
        composite.add(secondPuddle.present(secondViewer, object : ProxyDirector(director) {
            override fun onPosition(position: Frame) {
                welder.secondFrame = position
            }
        }))
        return composite
    }
}