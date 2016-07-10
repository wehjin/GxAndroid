package com.rubyhuntersky.gx.puddles.welders

import com.rubyhuntersky.gx.basics.Frame
import com.rubyhuntersky.gx.basics.Shift

/**
 * @author Jeffrey Yu
 * @since 7/9/16.
 */


fun createPoolRightWelder(anchor: Float): RandomAccessTwoFrameWelder {
    val onFrames: (Frame, Frame) -> TwoFrameWeld = { leftFrame, rightFrame ->
        val (leftHeight, rightHeight) = Pair(leftFrame.height, rightFrame.height)
        val (fullWidth, fullHeight) = Pair(leftFrame.width + rightFrame.width, Math.max(leftHeight, rightHeight))
        val (leftMargin, rightMargin) = Pair(fullHeight - leftHeight, fullHeight - rightHeight)
        val (leftDown, rightDown) = Pair(anchor * leftMargin, anchor * rightMargin)
        val (leftShift, rightShift) = Pair(Shift(0f, leftDown, 0f), Shift(leftFrame.width, rightDown, 0f))
        val fullFrame = Frame(fullWidth, fullHeight, 0)
        TwoFrameWeld(leftShift, rightShift, fullFrame)
    }
    return RandomAccessTwoFrameWelder.create(onFrames)
}
 
