package com.rubyhuntersky.tour

import android.os.Bundle
import android.support.v4.view.MotionEventCompat.getX
import android.support.v4.view.MotionEventCompat.getY
import android.support.v4.view.ViewCompat.setElevation
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.rubyhuntersky.coloret.Coloret.BLUE
import com.rubyhuntersky.coloret.Coloret.GREEN
import com.rubyhuntersky.gx.Gx.*
import com.rubyhuntersky.gx.android.AndroidHuman
import com.rubyhuntersky.gx.android.ShapeRuler
import com.rubyhuntersky.gx.android.TextRuler
import com.rubyhuntersky.gx.basics.*
import com.rubyhuntersky.gx.basics.Sizelet.FINGER
import com.rubyhuntersky.gx.basics.Sizelet.READABLE
import com.rubyhuntersky.gx.basics.TextStylet.*
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
import com.rubyhuntersky.gx.reactions.Reaction
import com.rubyhuntersky.gx.uis.divs.Div0
import java.util.*

open class MainActivity : AppCompatActivity() {
    companion object {
        val tag = MainActivity::class.java.simpleName
    }

    @BindView(R.id.main_frame)
    lateinit var mainFrame: FrameLayout

    fun FrameLayout.toPole(): Pole = Pole(width.toFloat(), 0f, 0, FrameLayoutScreen(this))

    val human by lazy { AndroidHuman(this) }
    val moreIndicator: Div0 by lazy {
        val moreMarker = textTile("▼", IMPORTANT_DARK)
        val moreMarkerInset = .1f
        val leftMarker = moreMarker.toColumn(moreMarkerInset)
        val rightMarker = moreMarker.toColumn(1f - moreMarkerInset)
        val moreIndicator = leftMarker.placeBefore(rightMarker, 0)
        moreIndicator
    }

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

        val menuLauncher = textColumn("Account 1234", TITLE_DARK)
                .padBottom(READABLE)
                .expandDown(textColumn("Buy 20 shares", READABLE_DARK))
                .padBottom(READABLE)
                .expandDown(textColumn("and", READABLE_DARK))
                .padBottom(Sizelet.readables(3f))
                .expandDown(textColumn("Add funds $3398.29", TITLE_DARK))
                .placeBefore(moreIndicator, 1, .5f)
                .padVertical(READABLE)
                .enableClick()

        val div = colorColumn(FINGER, GREEN)
                .expandDown(colorColumn(FINGER, BLUE))
                .expandDown(menuLauncher)

        div.present(human, container.toPole(), object : Observer {
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

    class FrameLayoutScreen(val frameLayout: FrameLayout) : Screen {

        val context = frameLayout.context
        val textRuler = TextRuler(context)
        val shapeRuler = ShapeRuler(context)
        val elevationPixels = context.resources.getDimensionPixelSize(com.rubyhuntersky.gx.R.dimen.elevationGap)
        val jesterItems = HashSet<JesterItem>()
        var contacts = HashSet<Jester.Contact>();

        private fun HashSet<Jester.Contact>.cancelAndClear() {
            for (contact in this) {
                contact.doCancel()
            }
            clear()
        }

        init {
            frameLayout.setOnTouchListener { view, motionEvent ->
                val spot = Spot(getX(motionEvent, 0), getY(motionEvent, 0), 0f)
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.v(tag, "Action Down")
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
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        Log.v(tag, "Action Cancel")
                        contacts.cancelAndClear()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        Log.v(tag, "Action Move")
                        val moveContacts = HashSet<Jester.Contact>()
                        val cancelContacts = HashSet<Jester.Contact>()
                        for (contact in contacts) {
                            val moveReaction = contact.getMoveReaction(spot)
                            if (moveReaction == MoveReaction.CONTINUE) {
                                moveContacts.add(contact)
                            } else {
                                contacts.remove(contact)
                                cancelContacts.add(contact)
                            }
                        }
                        cancelContacts.cancelAndClear()
                        for (contact in moveContacts) {
                            contact.doMove(spot)
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        Log.v(tag, "Action Up")
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
                }
                true
            }
        }

        override fun addSurface(frame: Frame, jester: Jester): Removable {
            Log.d(tag, "addSurface $frame, $jester")
            val jesterItem = JesterItem(frame, jester)
            jesterItems.add(jesterItem)
            return object : Removable {
                override fun remove() {
                    Log.d(tag, "removeSurface $jesterItem")
                    jesterItems.remove(jesterItem)
                }
            }
        }

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

        data class JesterItem(val frame: Frame, val jester: Jester);
    }
}
