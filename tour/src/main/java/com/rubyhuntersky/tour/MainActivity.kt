package com.rubyhuntersky.tour

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import butterknife.BindView
import butterknife.ButterKnife
import com.rubyhuntersky.coloret.Coloret.BLUE
import com.rubyhuntersky.coloret.Coloret.GREEN
import com.rubyhuntersky.gx.Gx.colorColumn
import com.rubyhuntersky.gx.android.AndroidHuman
import com.rubyhuntersky.gx.basics.*
import com.rubyhuntersky.gx.basics.Sizelet.FINGER
import com.rubyhuntersky.gx.devices.poles.Pole
import com.rubyhuntersky.gx.internal.devices.PatchDevice
import com.rubyhuntersky.gx.internal.patches.Patch
import com.rubyhuntersky.gx.internal.shapes.Shape
import com.rubyhuntersky.gx.observers.Observer
import com.rubyhuntersky.gx.reactions.Reaction

open class MainActivity : AppCompatActivity() {
    companion object {
        val tag = MainActivity::class.java.simpleName
    }

    @BindView(R.id.main_frame)
    lateinit var mainFrame: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        mainFrame.addView(object : FrameLayout(this) {
            override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
                super.onLayout(changed, left, top, right, bottom)
                if (changed) {
                    onWidth(this, left, right)
                }
            }
        }, MATCH_PARENT, MATCH_PARENT)
    }

    class FrameLayoutPatchDevice(val frameLayout: FrameLayout) : PatchDevice {
        override fun addPatch(frame: Frame, shape: Shape, argbColor: Int): Patch {
            Log.d(tag, "addPatch $frame $shape $argbColor")
            val view = View(frameLayout.context)
            view.setBackgroundColor(argbColor)
            val patchWidth = frame.horizontal.toLength().toInt()
            val patchHeight = frame.vertical.toLength().toInt()
            val layoutParams = FrameLayout.LayoutParams(patchWidth, patchHeight)
            layoutParams.leftMargin = frame.horizontal.start.toInt()
            layoutParams.topMargin = frame.vertical.start.toInt()
            frameLayout.addView(view, layoutParams)
            return Patch {
                frameLayout.removeView(view)
            }
        }

        override fun measureText(text: String, textStyle: TextStyle): TextSize {
            Log.d(tag, "measureText $text $textStyle")
            return TextSize(0f, TextHeight.ZERO)
        }

        override fun measureShape(shape: Shape): ShapeSize {
            throw UnsupportedOperationException()
        }
    }

    fun onWidth(container: FrameLayout, left: Int, right: Int) {
        Log.d(tag, "onWidth left $left right $right")
        val human = AndroidHuman(this)
        val patchDevice: PatchDevice = FrameLayoutPatchDevice(container)
        val div = colorColumn(FINGER, GREEN)
                .expandDown(colorColumn(FINGER, BLUE))
        div.present(human, Pole((right - left).toFloat(), 0f, 0, patchDevice), object : Observer {
            override fun onReaction(reaction: Reaction?) {
                Log.d(tag, "onReaction $reaction")
            }

            override fun onEnd() {
                Log.d(tag, "onEnd")
            }

            override fun onError(throwable: Throwable?) {
                Log.e(tag, "onError", throwable)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val width = mainFrame.width
        Log.d(tag, "onResume mainFrame width $width")
    }
}
