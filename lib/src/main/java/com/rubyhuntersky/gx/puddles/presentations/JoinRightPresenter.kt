package com.rubyhuntersky.gx.puddles.presentations

import com.rubyhuntersky.gx.basics.Frame
import com.rubyhuntersky.gx.basics.Shift
import com.rubyhuntersky.gx.puddles.Puddle
import com.rubyhuntersky.gx.puddles.welders.TwoFrameWeld

open class JoinRightPresenter(leftPuddle: Puddle, rightPuddle: Puddle, val anchor: Float) : TwoFrameWeldingPresenter(leftPuddle, rightPuddle) {
    override fun weldFrames(firstFrame: Frame, secondFrame: Frame): TwoFrameWeld {
        val (leftHeight, rightHeight) = Pair(firstFrame.height, secondFrame.height)
        val (fullWidth, fullHeight) = Pair(firstFrame.width + secondFrame.width, Math.max(leftHeight, rightHeight))
        val (leftMargin, rightMargin) = Pair(fullHeight - leftHeight, fullHeight - rightHeight)
        val (leftDown, rightDown) = Pair(anchor * leftMargin, anchor * rightMargin)
        val (leftShift, rightShift) = Pair(Shift(0f, leftDown, 0f), Shift(firstFrame.width, rightDown, 0f))
        val fullFrame = Frame(fullWidth, fullHeight, 0)
        return TwoFrameWeld(leftShift, rightShift, fullFrame)
    }
}
