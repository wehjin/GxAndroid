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
import com.rubyhuntersky.gx.basics.TextStylet.IMPORTANT_DARK
import com.rubyhuntersky.gx.basics.TextStylet.READABLE_DARK
import com.rubyhuntersky.gx.devices.poles.Pole
import com.rubyhuntersky.gx.observers.Observer
import com.rubyhuntersky.gx.presentations.BooleanPresentation
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
        val moreMarkerInset = .01f
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

    fun dropDownMenuDiv(startIndex: Int, items: List<Div0>): Div0 {
        return Div0.create({ presenter ->

            presenter.addPresentation(object : BooleanPresentation() {

                val pole = presenter.device
                val human = presenter.human
                var launcherPresentation: Presentation? = null
                var menuPresentation: Presentation? = null

                init {
                    presentLauncher(startIndex)
                }

                override fun getWidth(): Float {
                    return pole.fixedWidth
                }

                override fun getHeight(): Float {
                    return launcherPresentation?.height ?: 0f
                }

                override fun onCancel() {
                    menuPresentation?.cancel()
                    launcherPresentation?.cancel()
                }

                fun presentLauncher(index: Int) {
                    val item = items[index].padVertical(Sizelet.QUARTER_FINGER)
                    val launcher = item.placeBefore(moreIndicator, 1, .5f).enableTap("launcher")
                    launcherPresentation?.cancel()
                    launcherPresentation = launcher.present(human, pole, object : Observer {
                        override fun onReaction(reaction: Reaction) {
                            if (reaction is TapReaction<*>) {
                                presentMenu(index)
                            }
                        }

                        override fun onEnd() {
                            presenter.onEnd()
                        }

                        override fun onError(throwable: Throwable) {
                            presenter.onError(throwable)
                        }
                    })
                }

                fun presentMenu(launchIndex: Int) {
                    var menu: Div0? = null
                    var index = 0
                    for (item: Div0 in items) {
                        val paddedItem = item.padVertical(Sizelet.THIRD_FINGER).enableTap(index)
                                .enableTap(index)
                        menu = menu
                                ?.expandDown(colorColumn(Sizelet.readables(.2f), MAGENTA))
                                ?.expandDown(paddedItem)
                                ?: paddedItem
                        index++
                    }
                    menu = menu
                            ?.padVertical(Sizelet.READABLE)
                            ?.placeBefore(colorColumn(Sizelet.PREVIOUS, CYAN), 1)
                            ?: return

                    menuPresentation?.cancel()
                    menuPresentation = pole.present(menu, object : Observer {
                        override fun onReaction(reaction: Reaction) {
                            if (reaction is TapReaction<*>) {
                                menuPresentation?.cancel()
                                presentLauncher(reaction.tag as Int)
                            }
                        }

                        override fun onEnd() {
                            presenter.onEnd()
                        }

                        override fun onError(throwable: Throwable) {
                            presenter.onError(throwable)
                        }
                    })
                }
            })
        })
    }

    fun onWidth(frameLayout: FrameLayout, left: Int, right: Int) {
        Log.d(tag, "onWidth left $left right $right")
        val pole = Pole((right - left).toFloat(), 0f, 0, FrameLayoutScreen(frameLayout, human))
        val menuItem1 = textColumn("Account 1234", IMPORTANT_DARK)
                .padBottom(READABLE)
                .expandDown(textColumn("Buy 20 shares", READABLE_DARK))
                .padBottom(READABLE)
                .expandDown(textColumn("and", READABLE_DARK))
                .padBottom(Sizelet.readables(3f))
                .expandDown(textColumn("Add funds $3398.29", IMPORTANT_DARK))
                .padVertical(READABLE)
        val menuItem2 = textColumn("Account ABCD", IMPORTANT_DARK)
                .padBottom(READABLE)
                .expandDown(textColumn("Sufficient funds $8972.33", READABLE_DARK))
                .padVertical(READABLE)

        val menuItems = listOf(menuItem1, menuItem2)
        val dropDownMenu = dropDownMenuDiv(0, menuItems)

        val div = colorColumn(FINGER, GREEN)
                .expandDown(colorColumn(FINGER, BLUE))
                .expandDown(dropDownMenu)
        div.present(human, pole, LogObserver())
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
