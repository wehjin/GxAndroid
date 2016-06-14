package com.rubyhuntersky.gx.android

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.view.MotionEventCompat.getX
import android.support.v4.view.MotionEventCompat.getY
import android.support.v4.view.ViewCompat.setElevation
import android.util.Log
import android.view.*
import android.view.Window.FEATURE_NO_TITLE
import android.widget.FrameLayout
import android.widget.TextView
import com.rubyhuntersky.gx.Human
import com.rubyhuntersky.gx.R
import com.rubyhuntersky.gx.basics.*
import com.rubyhuntersky.gx.devices.poles.Pole
import com.rubyhuntersky.gx.devices.poles.ShiftPole
import com.rubyhuntersky.gx.internal.patches.Patch
import com.rubyhuntersky.gx.internal.screen.Screen
import com.rubyhuntersky.gx.internal.shapes.RectangleShape
import com.rubyhuntersky.gx.internal.shapes.Shape
import com.rubyhuntersky.gx.internal.shapes.TextShape
import com.rubyhuntersky.gx.internal.shapes.ViewShape
import com.rubyhuntersky.gx.internal.surface.Jester
import com.rubyhuntersky.gx.internal.surface.MoveReaction
import com.rubyhuntersky.gx.internal.surface.UpReaction
import com.rubyhuntersky.gx.reactions.Reaction
import com.rubyhuntersky.gx.uis.divs.Div
import com.rubyhuntersky.gx.uis.divs.Div0
import com.rubyhuntersky.gx.uis.divs.TapContact
import java.util.*

class FrameLayoutScreen(val frameLayout: FrameLayout, val human: Human, val activity: FragmentActivity, val onClick: (() -> Unit)? = null) : Screen {

    val tag = "${FrameLayoutScreen::class.java.simpleName}${this.hashCode()}"
    val context = frameLayout.context
    val textRuler = TextRuler(context)
    val shapeRuler = ShapeRuler(context)
    val patchController = PatchController(frameLayout)
    val surfaceController = TouchController(human, object : Div.Observer {
        override fun onHeight(height: Float) {
        }

        override fun onReaction(reaction: Reaction) {
            Log.d(tag, "onTap $reaction")
            onClick?.invoke()
        }

        override fun onError(throwable: Throwable) {
        }
    })

    init {
        frameLayout.setOnTouchListener(surfaceController.newTouchListener)
    }

    class DialogFragmentDivPresentation(val activity: FragmentActivity, val fragmentManager: FragmentManager, val viewWidth: Int,
                                        val div: Div0, val offset: Float, val human: Human, val observer: Div.Observer) : Div.BooleanPresentation() {
        val tag = DialogFragmentDivPresentation::class.java.simpleName
        val dialogFragmentTag = "DivFragment${this@DialogFragmentDivPresentation.hashCode()}"

        init {
            Log.d(tag, "init $viewWidth")
            val dialogFragment: DialogFragment = object : DialogFragment() {

                var pole: ShiftPole? = null
                var presentation: Div.Presentation? = null
                var viewHeight: Int = 1

                override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
                    val dialog = super.onCreateDialog(savedInstanceState)
                    dialog.window.requestFeature(FEATURE_NO_TITLE);
                    dialog.window.setBackgroundDrawable(ColorDrawable(0))
                    return dialog
                }

                override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
                    val view: FrameLayout = object : FrameLayout(this@DialogFragmentDivPresentation.activity) {
                        init {
                            setBackgroundColor(0x00FF0000.toInt())
                        }

                        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
                            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
                            Log.d(this@DialogFragmentDivPresentation.tag, "onMeasure ${MeasureSpec.toString(widthMeasureSpec)} ${MeasureSpec.toString(heightMeasureSpec)} $viewWidth $viewHeight")
                            setMeasuredDimension(viewWidth, viewHeight)
                        }
                    }
                    container?.addView(view)
                    pole = Pole(viewWidth.toFloat(), 0f, 100, FrameLayoutScreen(view, human, activity, {
                        dismiss()
                    })).withShift(0f, offset)
                    return view
                }

                override fun onResume() {
                    super.onResume()
                    presentation = div.present(human, pole!!, object : Div.ForwardingObserver(observer) {
                        override fun onHeight(height: Float) {
                            val divHeight = height.toInt()
                            Log.d(this@DialogFragmentDivPresentation.tag, "onHeight $divHeight")
                            if (viewHeight != divHeight) {
                                val positiveOffset = Math.abs(offset)
                                viewHeight = (divHeight + 2 * positiveOffset).toInt()
                                pole!!.doShift(0f, offset + positiveOffset)
                                view?.requestLayout()
                            }
                            super.onHeight(height)
                        }
                    })
                    Log.d(this@DialogFragmentDivPresentation.tag, "onResume")
                }

                override fun onPause() {
                    Log.d(this@DialogFragmentDivPresentation.tag, "onPause")
                    presentation?.cancel()
                    super.onPause()
                }
            }
            dialogFragment.show(fragmentManager, dialogFragmentTag)
        }

        override fun onCancel() {
            Log.d(tag, "onCancel")
            val dialogFragment = fragmentManager.findFragmentByTag(dialogFragmentTag) as DialogFragment?
            Log.d(tag, "onCancel $dialogFragment")
            dialogFragment?.dismiss()
        }
    }

    override fun present(div: Div0, offset: Float, observer: Div.Observer): Div.Presentation {
        val presentation = DialogFragmentDivPresentation(activity, activity.supportFragmentManager, frameLayout.width, div, offset, human, observer)
        return presentation
    }

    override fun addSurface(frame: Frame, jester: Jester): Removable {
        return surfaceController.addSurface(frame, jester)
    }

    override fun addPatch(frame: Frame, shape: Shape, argbColor: Int): Patch {
        return patchController.addPatch(frame, shape, argbColor)

    }

    override fun measureText(text: String, textStyle: TextStyle): TextSize {
        Log.d(tag, "measureText $text $textStyle")
        return textRuler.measure(text, textStyle)
    }

    override fun measureShape(shape: Shape): ShapeSize {
        Log.d(tag, "measureShape $shape")
        return shapeRuler.measure(shape)
    }

    inner class PatchController(val frameLayout: FrameLayout) {

        val context = frameLayout.context
        val resources = context.resources
        val elevationPixels = resources.getDimensionPixelSize(R.dimen.elevationGap)

        fun addPatch(frame: Frame, shape: Shape, argbColor: Int): Patch {
            Log.d(tag, "addPatch $frame $shape $argbColor")
            when (shape) {
                is RectangleShape -> {
                    Log.d(tag, "RectangleShape")
                    val view = View(context)
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

    class TouchController(val human: Human, val observer: Div.Observer) {
        companion object {
            val tag = TouchController::class.java.simpleName
        }

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
            Log.v(tag, "addSurface $frame, $jester $this")
            val jesterItem = JesterItem(frame, jester)
            jesterItems.add(jesterItem)
            Log.v(tag, "addSurface done remaining $jesterItems $this")
            return object : Removable {
                override fun remove() {
                    Log.v(tag, "removeSurface $jesterItem ${this@TouchController}")
                    jesterItems.remove(jesterItem)
                    Log.v(tag, "removeSurface remaining $jesterItems ${this@TouchController}")
                }
            }
        }

        private fun onTouchDown(spot: Spot) {
            Log.d(tag, "Action Down $jesterItems $this")
            contacts.cancelAndClear()
            for ((frame, jester) in jesterItems) {
                Log.v(tag, "Spot $spot, Frame $frame")
                if (spot.intersects(frame)) {
                    val contact = jester.getContact(spot)
                    Log.v(tag, "Contact $contact")
                    if (contact != null) {
                        contacts.add(contact)
                    }
                }
            }
            if (contacts.isEmpty()) {
                contacts.add(TapContact(spot, observer, human, "Background"))
            }
        }

        private fun onTouchCancel() {
            Log.d(tag, "Action Cancel $jesterItems")
            contacts.cancelAndClear()
        }

        private fun onTouchMove(spot: Spot) {
            Log.v(tag, "Action Move")
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
            Log.d(tag, "Action Up $jesterItems")
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
            Log.d(tag, "Action Up done $jesterItems $this")
        }

        private fun HashSet<Jester.Contact>.cancelAndClear() {
            for (contact in this) {
                contact.doCancel()
            }
            clear()
        }
    }
}