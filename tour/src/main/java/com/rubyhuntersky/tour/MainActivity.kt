package com.rubyhuntersky.tour

import android.os.Bundle
import android.support.v4.view.ViewCompat.setElevation
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.rubyhuntersky.coloret.Coloret.BLUE
import com.rubyhuntersky.coloret.Coloret.GREEN
import com.rubyhuntersky.gx.Gx
import com.rubyhuntersky.gx.Gx.colorColumn
import com.rubyhuntersky.gx.android.AndroidHuman
import com.rubyhuntersky.gx.android.ShapeRuler
import com.rubyhuntersky.gx.android.TextRuler
import com.rubyhuntersky.gx.basics.Frame
import com.rubyhuntersky.gx.basics.ShapeSize
import com.rubyhuntersky.gx.basics.Sizelet.FINGER
import com.rubyhuntersky.gx.basics.TextSize
import com.rubyhuntersky.gx.basics.TextStyle
import com.rubyhuntersky.gx.basics.TextStylet.TITLE_DARK
import com.rubyhuntersky.gx.devices.poles.Pole
import com.rubyhuntersky.gx.internal.devices.PatchDevice
import com.rubyhuntersky.gx.internal.patches.Patch
import com.rubyhuntersky.gx.internal.shapes.RectangleShape
import com.rubyhuntersky.gx.internal.shapes.Shape
import com.rubyhuntersky.gx.internal.shapes.TextShape
import com.rubyhuntersky.gx.internal.shapes.ViewShape
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

    fun onWidth(container: FrameLayout, left: Int, right: Int) {
        Log.d(tag, "onWidth left $left right $right")
        val human = AndroidHuman(this)
        val patchDevice: PatchDevice = FrameLayoutPatchDevice(container)
        val div = colorColumn(FINGER, GREEN)
                .expandDown(colorColumn(FINGER, BLUE))
                .expandDown(Gx.textColumn("Hello", TITLE_DARK))
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

    class FrameLayoutPatchDevice(val frameLayout: FrameLayout) : PatchDevice {

        val context = frameLayout.context
        val textRuler = TextRuler(context)
        val shapeRuler = ShapeRuler(context)
        val elevationPixels = context.resources.getDimensionPixelSize(com.rubyhuntersky.gx.R.dimen.elevationGap)

        override fun addPatch(frame: Frame, shape: Shape, argbColor: Int): Patch {

            Log.d(tag, "addPatch $frame $shape $argbColor")
            when (shape) {
                is RectangleShape -> {
                    Log.d(tag, "RectangleShape")
                    val view = View(frameLayout.context)
                    view.setBackgroundColor(argbColor)
                    return addPatch(view, frame, 0f)
                }
                is TextShape -> {
                    Log.d(tag, "TextShape")
                    val textView = TextView(context)
                    textView.gravity = Gravity.TOP
                    textView.setTextColor(shape.textStyle.typecolor)
                    textView.typeface = shape.textStyle.typeface
                    textView.textSize = shape.textStyle.typeheight.toFloat()
                    textView.text = shape.textString
                    textView.includeFontPadding = false
                    val textHeight = shape.textSize.textHeight
                    val newFrame = frame.withVerticalShift(-textHeight.topPadding).withVerticalLength(textHeight.topPadding + textHeight.height)
                    return addPatch(textView, newFrame, textHeight.height / 2)
                }
                is ViewShape -> {
                    val view = shape.createView(context)
                    return addPatch(view, frame, 0f)
                }
                else -> {
                    Log.d(tag, "OtherShape")
                    return Patch.EMPTY
                }
            }
        }

        private fun addPatch(view: View, frame: Frame, additionalHeight: Float): Patch {
            setElevation(view, (elevationPixels * frame.elevation).toFloat())
            frameLayout.addView(view, frame.toLayoutParams(additionalHeight))
            return Patch {
                frameLayout.removeView(view)
            }
        }

        private fun Frame.toLayoutParams(additionalHeight: Float): FrameLayout.LayoutParams {
            val patchWidth = horizontal.toLength().toInt()
            val patchHeight = (vertical.toLength() + additionalHeight).toInt()
            val layoutParams = FrameLayout.LayoutParams(patchWidth, patchHeight)
            layoutParams.leftMargin = horizontal.start.toInt()
            layoutParams.topMargin = vertical.start.toInt()
            return layoutParams
        }

        override fun measureText(text: String, textStyle: TextStyle): TextSize {
            Log.d(tag, "measureText $text $textStyle")
            return textRuler.measure(text, textStyle)
        }

        override fun measureShape(shape: Shape): ShapeSize {
            Log.d(tag, "measureShape $shape")
            return shapeRuler.measure(shape)

        }
    }
}
