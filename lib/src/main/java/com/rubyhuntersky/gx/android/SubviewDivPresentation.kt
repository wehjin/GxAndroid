package com.rubyhuntersky.gx.android

import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewCompat
import android.util.Log
import android.widget.FrameLayout
import com.rubyhuntersky.gx.Human
import com.rubyhuntersky.gx.basics.Sizelet
import com.rubyhuntersky.gx.devices.poles.Pole
import com.rubyhuntersky.gx.uis.divs.Div
import com.rubyhuntersky.gx.uis.divs.Div0

class SubviewDivPresentation(val div: Div0, val human: Human, val observer: Div.Observer, val frameLayout: FrameLayout, val activity: FragmentActivity) : Div.BooleanPresentation() {

    var tag = SubviewDivPresentation::class.java.simpleName
    var subFrame: FrameLayout? = null
    var subPresentation: Div.Presentation? = null

    init {
        subFrame = object : FrameLayout(frameLayout.context) {
            private val screen = FrameLayoutScreen(this, human, activity)

            init {
                setBackgroundColor(0xc0000000.toInt())
                ViewCompat.setElevation(this, 100f) // TODO set real elevation
            }

            override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
                super.onLayout(changed, left, top, right, bottom)
                if (changed) {
                    this.postDelayed({ onWidth(right - left) }, 1)
                }
            }

            fun onWidth(width: Int) {
                val pole = Pole(width.toFloat(), 0f, 0, screen).withShift();
                subPresentation?.cancel()
                subPresentation = div.padHorizontal(Sizelet.DOUBLE_READABLE)
                        .present(human, pole, object : Div.ForwardingObserver(observer) {
                            override fun onHeight(height: Float) {
                                val extraHeight = subFrame!!.height - height
                                pole.doShift(0f, extraHeight / 3)
                                super.onHeight(height)
                            }
                        })
            }
        }

        Log.d(tag, "subpresent ${subFrame!!.hashCode()}")
        frameLayout.addView(subFrame, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
    }

    override fun onCancel() {
        subPresentation?.cancel()
        frameLayout.removeView(subFrame!!)
        Log.d(tag, "subpresent cancel ${subFrame!!.hashCode()}")
    }
}