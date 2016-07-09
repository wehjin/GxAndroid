package com.rubyhuntersky.tour

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.rubyhuntersky.gx.Human
import com.rubyhuntersky.gx.android.TextRuler
import com.rubyhuntersky.gx.android.elevationCompat
import com.rubyhuntersky.gx.android.toTextView
import com.rubyhuntersky.gx.basics.*
import com.rubyhuntersky.gx.internal.shapes.Shape
import com.rubyhuntersky.gx.internal.shapes.TextShape
import com.rubyhuntersky.gx.puddles.Puddle
import com.rubyhuntersky.gx.puddles.textLinePuddle
import com.rubyhuntersky.gx.reactions.Reaction

class TourPuddlesActivity : AppCompatActivity(), Puddle.Viewer {

    companion object {
        val TAG = TourPuddlesActivity::class.java.simpleName!!
    }

    val textRuler by lazy { TextRuler(this) }

    val frameView: FrameLayout by lazy {
        findViewById(R.id.puddlesFrame)!! as FrameLayout
    }

    override val universe: Space by lazy {
        val width = frameView.width.toFloat()
        val height = frameView.height.toFloat()
        Space(Range(width), Range(height), Range(-50f, 50f))
    }

    override val human: Human by lazy {
        val fingerTipPixels = resources.getDimensionPixelSize(R.dimen.fingerTip)
        val textLinePixels = resources.getDimensionPixelSize(R.dimen.textLine)
        Human(fingerTipPixels.toFloat(), textLinePixels.toFloat())
    }

    var presentation: Puddle.Presentation? = null

    override fun getTextSize(text: String, style: TextStyle): TextSize {
        return textRuler.measure(text, style)
    }

    override fun addPatch(id: Long, position: Frame, color: Int, shape: Shape) {
        if (shape is TextShape) {
            val textView = shape.toTextView(this)
            val padding = shape.textSize.textHeight.topPadding.toInt()
            textView.setPadding(0, padding, 0, padding)
            val newTop = position.top - padding
            val newBottom = position.bottom + padding
            addPatchView(textView, id, position.left, newTop, position.right, newBottom, position.elevation)
        } else {
            val view = View(this)
            view.setBackgroundColor(color)
            addPatchView(view, id, position.left, position.top, position.width, position.height, position.elevation)
        }
    }

    private fun addPatchView(view: View, id: Long, left: Float, top: Float, width: Float, height: Float, elevation: Int) {
        removePatch(id)
        view.id = id.toInt()
        view.elevationCompat = elevation.toFloat()
        val layoutParams = FrameLayout.LayoutParams(width.toInt(), height.toInt())
        layoutParams.leftMargin = left.toInt()
        layoutParams.topMargin = top.toInt()
        frameView.addView(view, layoutParams)
    }

    override fun removePatch(id: Long) {
        val view = frameView.findViewById(id.toInt())
        if (view != null) {
            frameView.removeView(view)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tour_puddles)
    }

    override fun onResume() {
        super.onResume()
        frameView.postDelayed({
            val touchSize = human.fingerPixels
            val spacing = touchSize / 3
            val color = ContextCompat.getColor(this, R.color.tour1)
            val bodyLineHeight = human.textPixels
            val titleLineHeight = bodyLineHeight * 1.25f
            val textStyle = TextStyle(titleLineHeight, Typeface.DEFAULT_BOLD, color)
            val text = "Hello"
            val puddle = textLinePuddle(text, textStyle).padOut(spacing)
            presentation = puddle.present(this, object : Puddle.Director {
                override fun onPosition(position: Frame) {
                    Log.d(TAG, "Position $position")
                }

                override fun onReaction(reaction: Reaction) {
                    Log.d(TAG, "Reaction $reaction")
                }

                override fun onError(throwable: Throwable) {
                    Log.e(TAG, "onResume", throwable)
                }
            })
        }, 0)
    }

    override fun onPause() {
        presentation?.end()
        super.onPause()
    }
}
