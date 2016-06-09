package com.rubyhuntersky.tour

import android.support.v4.view.MotionEventCompat.getX
import android.support.v4.view.MotionEventCompat.getY
import android.support.v4.view.ViewCompat.setElevation
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import android.widget.TextView
import com.rubyhuntersky.gx.Human
import com.rubyhuntersky.gx.R
import com.rubyhuntersky.gx.android.ShapeRuler
import com.rubyhuntersky.gx.android.TextRuler
import com.rubyhuntersky.gx.basics.*
import com.rubyhuntersky.gx.devices.poles.Pole
import com.rubyhuntersky.gx.internal.patches.Patch
import com.rubyhuntersky.gx.internal.screen.Screen
import com.rubyhuntersky.gx.internal.shapes.RectangleShape
import com.rubyhuntersky.gx.internal.shapes.Shape
import com.rubyhuntersky.gx.internal.shapes.TextShape
import com.rubyhuntersky.gx.internal.shapes.ViewShape
import com.rubyhuntersky.gx.internal.surface.Jester
import com.rubyhuntersky.gx.internal.surface.MoveReaction
import com.rubyhuntersky.gx.internal.surface.UpReaction
import com.rubyhuntersky.gx.observers.Observer
import com.rubyhuntersky.gx.presentations.Presentation
import com.rubyhuntersky.gx.uis.divs.Div0
import java.util.*

class FrameLayoutScreen(val frameLayout: FrameLayout, val human: Human) : Screen {

    val tag = "${FrameLayoutScreen::class.java.simpleName}${this.hashCode()}"
    val context = frameLayout.context
    val textRuler = TextRuler(context)
    val shapeRuler = ShapeRuler(context)
    val patchController = PatchController(frameLayout)
    val surfaceController = TouchController()

    init {
        frameLayout.setOnTouchListener(surfaceController.newTouchListener)
    }

    override fun present(div: Div0, observer: Observer): Presentation {
        return object : Presentation {
            var cancelled = false
            var subPresentation: Presentation? = null
            val subFrameElevation = 100f
            val subFrame = object : FrameLayout(context) {
                private val screen = FrameLayoutScreen(this, human)

                fun onWidth(width: Int) {
                    val pole = Pole(width.toFloat(), 0f, 0, screen)
                    subPresentation?.cancel()
                    subPresentation = div.present(human, pole, observer)
                }

                override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
                    super.onLayout(changed, left, top, right, bottom)
                    if (changed) {
                        this.postDelayed({ onWidth(right - left) }, 1)
                    }
                }
            }

            init {
                Log.d(tag, "subpresent ${subFrame.hashCode()}")
                subFrame.setBackgroundColor(0xc0000000.toInt())
                setElevation(subFrame, subFrameElevation)
                frameLayout.addView(subFrame, MATCH_PARENT, MATCH_PARENT)
            }

            override fun getWidth(): Float {
                return width
            }

            override fun getHeight(): Float {
                return height
            }

            override fun isCancelled(): Boolean {
                return cancelled
            }

            override fun cancel() {
                if (!cancelled) {
                    Log.d(tag, "subpresent cancel ${subFrame.hashCode()}")
                    cancelled = true
                    subPresentation?.cancel()
                    frameLayout.removeView(subFrame)
                }
            }
        }
    }

    override fun addSurface(frame: Frame, jester: Jester): Removable {
        return surfaceController.addSurface(frame, jester)
    }

    override fun addPatch(frame: Frame, shape: Shape, argbColor: Int): Patch {
        return patchController.addPatch(frame, shape, argbColor)

    }

    override fun measureText(text: String, textStyle: TextStyle): TextSize {
        Log.d(MainActivity.tag, "measureText $text $textStyle")
        return textRuler.measure(text, textStyle)
    }

    override fun measureShape(shape: Shape): ShapeSize {
        Log.d(MainActivity.tag, "measureShape $shape")
        return shapeRuler.measure(shape)
    }

    class PatchController(val frameLayout: FrameLayout) {

        val context = frameLayout.context
        val resources = context.resources
        val elevationPixels = resources.getDimensionPixelSize(R.dimen.elevationGap)

        fun addPatch(frame: Frame, shape: Shape, argbColor: Int): Patch {
            Log.d(MainActivity.tag, "addPatch $frame $shape $argbColor")
            when (shape) {
                is RectangleShape -> {
                    Log.d(MainActivity.tag, "RectangleShape")
                    val view = View(context)
                    view.setBackgroundColor(argbColor)
                    return addPatch(view, frame, 0f)
                }
                is TextShape -> {
                    Log.d(MainActivity.tag, "TextShape")
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
                    Log.d(MainActivity.tag, "OtherShape")
                    return com.rubyhuntersky.gx.internal.patches.Patch.EMPTY
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
    }

    class TouchController {

        private data class JesterItem(val frame: Frame, val jester: Jester)

        private val jesterItems = HashSet<JesterItem>()
        private val contacts = HashSet<Jester.Contact>()

        val newTouchListener: (View, MotionEvent) -> Boolean get() = { view, motionEvent ->
            val spot = Spot(getX(motionEvent, 0), getY(motionEvent, 0), 0f)
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    onTouchDown(spot)
                }
                MotionEvent.ACTION_CANCEL -> {
                    onTouchCancel()
                }
                MotionEvent.ACTION_MOVE -> {
                    onTouchMove(spot)
                }
                MotionEvent.ACTION_UP -> {
                    onTouchUp(spot)
                }
            }
            true
        }

        fun addSurface(frame: Frame, jester: Jester): Removable {
            Log.d(MainActivity.tag, "addSurface $frame, $jester")
            val jesterItem = JesterItem(frame, jester)
            jesterItems.add(jesterItem)
            return object : Removable {
                override fun remove() {
                    Log.d(MainActivity.tag, "removeSurface $jesterItem")
                    jesterItems.remove(jesterItem)
                }
            }
        }

        private fun onTouchDown(spot: Spot) {
            Log.v(MainActivity.tag, "Action Down $this")
            contacts.cancelAndClear()
            for ((frame, jester) in jesterItems) {
                Log.v(MainActivity.tag, "Spot $spot, Frame $frame")
                if (spot.intersects(frame)) {
                    val contact = jester.getContact(spot)
                    Log.v(MainActivity.tag, "Contact $contact")
                    if (contact != null) {
                        contacts.add(contact)
                    }
                }
            }
        }

        private fun onTouchCancel() {
            Log.v(MainActivity.tag, "Action Cancel")
            contacts.cancelAndClear()
        }

        private fun onTouchMove(spot: Spot) {
            Log.v(MainActivity.tag, "Action Move")
            val moveContacts = HashSet<Jester.Contact>()
            val cancelContacts = HashSet<Jester.Contact>()
            for (contact in contacts) {
                val moveReaction = contact.getMoveReaction(spot)
                if (moveReaction == MoveReaction.CONTINUE) {
                    moveContacts.add(contact)
                } else {
                    cancelContacts.add(contact)
                }
            }
            contacts.removeAll(cancelContacts)
            cancelContacts.cancelAndClear()
            for (contact in moveContacts) {
                contact.doMove(spot)
            }
        }

        private fun onTouchUp(spot: Spot) {
            Log.v(MainActivity.tag, "Action Up $this")
            val upContacts = HashSet<Jester.Contact>()
            val cancelContacts = HashSet<Jester.Contact>()
            for (contact in contacts) {
                if (contact.getUpReaction(spot) == UpReaction.CONFIRM) {
                    upContacts.add(contact)
                } else {
                    cancelContacts.add(contact)
                }
            }
            contacts.clear()
            cancelContacts.cancelAndClear()
            for (contact in upContacts) {
                contact.doUp(spot)
            }
        }

        private fun HashSet<Jester.Contact>.cancelAndClear() {
            for (contact in this) {
                contact.doCancel()
            }
            clear()
        }
    }
}