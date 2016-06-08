package com.rubyhuntersky.tour

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import butterknife.BindView
import butterknife.ButterKnife
import com.rubyhuntersky.coloret.Coloret.*
import com.rubyhuntersky.gx.Gx.*
import com.rubyhuntersky.gx.android.AndroidHuman
import com.rubyhuntersky.gx.basics.Sizelet
import com.rubyhuntersky.gx.basics.Sizelet.FINGER
import com.rubyhuntersky.gx.basics.Sizelet.READABLE
import com.rubyhuntersky.gx.basics.TextStylet.*
import com.rubyhuntersky.gx.devices.poles.Pole
import com.rubyhuntersky.gx.observers.Observer
import com.rubyhuntersky.gx.presentations.Presentation
import com.rubyhuntersky.gx.reactions.Reaction
import com.rubyhuntersky.gx.reactions.TapReaction
import com.rubyhuntersky.gx.uis.divs.Div0

open class MainActivity : AppCompatActivity() {
    companion object {
        val tag = MainActivity::class.java.simpleName
    }

    @BindView(R.id.main_frame)
    lateinit var mainFrame: FrameLayout

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

    fun onWidth(frameLayout: FrameLayout, left: Int, right: Int) {
        Log.d(tag, "onWidth left $left right $right")
        val pole = Pole((right - left).toFloat(), 0f, 0, FrameLayoutScreen(frameLayout, human))
        val menuLauncher = textColumn("Account 1234", TITLE_DARK)
                .padBottom(READABLE)
                .expandDown(textColumn("Buy 20 shares", READABLE_DARK))
                .padBottom(READABLE)
                .expandDown(textColumn("and", READABLE_DARK))
                .padBottom(Sizelet.readables(3f))
                .expandDown(textColumn("Add funds $3398.29", TITLE_DARK))
                .placeBefore(moreIndicator, 1, .5f)
                .padVertical(READABLE)
                .enableTap()

        val div = colorColumn(FINGER, GREEN)
                .expandDown(colorColumn(FINGER, BLUE))
                .expandDown(menuLauncher)

        div.present(human, pole, object : LogObserver() {

            var menuPresentation: Presentation? = null

            override fun onReaction(reaction: Reaction) {
                super.onReaction(reaction)
                if (reaction is TapReaction) {
                    val overDiv = colorColumn(FINGER, RED).enableTap()
                    menuPresentation = pole.present(overDiv, object : LogObserver() {
                        override fun onReaction(reaction: Reaction) {
                            super.onReaction(reaction)
                            when (reaction) {
                                is TapReaction -> {
                                    menuPresentation?.cancel()
                                    menuPresentation = null
                                }
                            }
                        }
                    })
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val width = mainFrame.width
        Log.d(tag, "onResume mainFrame width $width")
    }

    open class LogObserver : Observer {

        val tag = "${LogObserver::class.java.simpleName}${this.hashCode()}"

        override fun onReaction(reaction: Reaction) {
            Log.d(tag, "onReaction $reaction")
        }

        override fun onEnd() {
            Log.d(tag, "onEnd")
        }

        override fun onError(throwable: Throwable) {
            Log.e(tag, "onError", throwable)
        }
    }
}
