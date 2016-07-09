package com.rubyhuntersky.gx.android

import android.content.Context
import android.support.v4.view.ViewCompat
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.rubyhuntersky.gx.internal.shapes.TextShape

/**
 * @author Jeffrey Yu
 * @since 7/9/16.
 */

fun TextShape.toTextView(context: Context): TextView {
    val textView = TextView(context)
    textView.gravity = Gravity.TOP
    textView.setTextColor(textStyle.typecolor)
    textView.typeface = textStyle.typeface
    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textStyle.typeheight.toFloat())
    textView.text = textString
    textView.includeFontPadding = false
    return textView
}

var View.elevationCompat: Float
    get() = this.elevation
    set(value) = ViewCompat.setElevation(this, value)

 
