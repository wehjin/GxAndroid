package com.rubyhuntersky.tour

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import butterknife.BindView
import butterknife.ButterKnife

open class MainActivity : AppCompatActivity() {

    @BindView(R.id.main_frame)
    lateinit var mainFrame: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.setDebug(true)

        ButterKnife.bind(this)
        mainFrame.addView(object : View(this) {
            override fun onAttachedToWindow() {
                super.onAttachedToWindow()
                setBackgroundColor(Color.CYAN)
            }

            override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
                super.onSizeChanged(width, height, oldWidth, oldHeight)
            }
        }, MATCH_PARENT, MATCH_PARENT)
    }
}
