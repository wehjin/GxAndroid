package com.rubyhuntersky.tour

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import com.rubyhuntersky.gx.Human
import com.rubyhuntersky.gx.basics.Frame
import com.rubyhuntersky.gx.basics.Range
import com.rubyhuntersky.gx.basics.Space
import com.rubyhuntersky.gx.puddles.Puddle
import com.rubyhuntersky.gx.puddles.Puddles

class TourPuddlesActivity : AppCompatActivity(), Puddle.Viewer {

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

    override fun addPatch(id: Long, position: Frame, color: Int) {
        val view = View(this)
        view.id = id.toInt()
        ViewCompat.setElevation(view, position.elevation.toFloat())
        view.setBackgroundColor(color)
        val layoutParams = FrameLayout.LayoutParams(position.width.toInt(), position.height.toInt())
        layoutParams.leftMargin = position.left.toInt()
        layoutParams.topMargin = position.top.toInt()
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
            val width = universe.width.toLength() / 2
            val height = human.fingerPixels
            val color = ContextCompat.getColor(this, R.color.tour1)
            val puddle = Puddles.colorPuddle(width, height, color)
            presentation = puddle.present(this, Puddles.EMPTY_DIRECTOR)
        }, 0)
    }

    override fun onPause() {
        presentation?.end()
        super.onPause()
    }
}
