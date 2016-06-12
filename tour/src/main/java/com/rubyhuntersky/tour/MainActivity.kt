package com.rubyhuntersky.tour

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import butterknife.BindView
import butterknife.ButterKnife
import com.rubyhuntersky.coloret.Coloret.*
import com.rubyhuntersky.gx.Gx.colorColumn
import com.rubyhuntersky.gx.Gx.dropDownMenuDiv
import com.rubyhuntersky.gx.Gx.textColumn
import com.rubyhuntersky.gx.android.AndroidHuman
import com.rubyhuntersky.gx.android.FrameLayoutScreen
import com.rubyhuntersky.gx.basics.Sizelet
import com.rubyhuntersky.gx.basics.Sizelet.FINGER
import com.rubyhuntersky.gx.basics.Sizelet.READABLE
import com.rubyhuntersky.gx.basics.TextStylet.IMPORTANT_DARK
import com.rubyhuntersky.gx.basics.TextStylet.READABLE_DARK
import com.rubyhuntersky.gx.devices.poles.Pole
import com.rubyhuntersky.gx.reactions.Reaction
import com.rubyhuntersky.gx.uis.divs.Div

open class MainActivity : AppCompatActivity() {
    companion object {
        val tag = MainActivity::class.java.simpleName
    }

    @BindView(R.id.main_frame)
    lateinit var mainFrame: FrameLayout

    val human by lazy { AndroidHuman(this) }

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
                .expandDown(colorColumn(FINGER, RED))

        div.present(human, pole, LogObserver())
    }

    override fun onResume() {
        super.onResume()
        val width = mainFrame.width
        Log.d(tag, "onResume mainFrame width $width")
    }

    open class LogObserver : Div.Observer {

        val tag = "${LogObserver::class.java.simpleName}${this.hashCode()}"

        override fun onHeight(height: Float) {
            Log.d(tag, "onHeight $height")
        }

        override fun onReaction(reaction: Reaction) {
            Log.d(tag, "onReaction $reaction")
        }

        override fun onError(throwable: Throwable) {
            Log.e(tag, "onError", throwable)
        }
    }
}
