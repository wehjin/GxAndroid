package com.rubyhuntersky.gx.puddles.welders

import com.rubyhuntersky.gx.basics.Frame
import com.rubyhuntersky.gx.basics.Shift

data class TwoFrameWeld(val leftShift: Shift, val rightShift: Shift, val fullFrame: Frame)