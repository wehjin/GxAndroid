package com.rubyhuntersky.gx

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.rubyhuntersky.coloret.Coloret
import com.rubyhuntersky.gx.basics.Frame
import com.rubyhuntersky.gx.basics.Sizelet
import com.rubyhuntersky.gx.basics.TextStylet
import com.rubyhuntersky.gx.internal.shapes.RectangleShape
import com.rubyhuntersky.gx.internal.shapes.ViewShape
import com.rubyhuntersky.gx.observers.Observer
import com.rubyhuntersky.gx.presentations.BooleanPresentation
import com.rubyhuntersky.gx.presentations.PatchPresentation
import com.rubyhuntersky.gx.presentations.Presentation
import com.rubyhuntersky.gx.reactions.Reaction
import com.rubyhuntersky.gx.reactions.TapReaction
import com.rubyhuntersky.gx.uis.divs.Div0
import com.rubyhuntersky.gx.uis.spans.Span0
import com.rubyhuntersky.gx.uis.tiles.Tile0

/**
 * @author wehjin
 * *
 * @since 1/23/16.
 */

object Gx {

    val TAG = Gx::class.java.simpleName

    fun textTile(textString: String, textStylet: TextStylet): Tile0 {
        return Tile0.create { presenter ->
            val human = presenter.human
            val mosaic = presenter.device
            val textStyle = textStylet.toStyle(human, mosaic.relatedHeight)
            val textSize = mosaic.measureText(textString, textStyle)
            val frame = Frame(textSize.textWidth, textSize.textHeight.height, mosaic.elevation)
            val textHeight = textSize.textHeight
            val textFrame = frame.withVerticalShift(-textHeight.topPadding).withVerticalLength(textHeight.topPadding + 1.5f * textHeight.height)
            val viewShape = object : ViewShape() {
                override fun createView(context: Context): View {
                    val textView = TextView(context)
                    textView.gravity = Gravity.TOP
                    textView.setSingleLine()
                    textView.setTextColor(textStyle.typecolor)
                    textView.typeface = textStyle.typeface
                    textView.textSize = textStyle.typeheight.toFloat()
                    textView.text = textString
                    textView.includeFontPadding = false
                    textView.contentDescription = "TextTile"
                    return textView
                }
            }
            val patch = mosaic.addPatch(textFrame, viewShape, textStyle.typecolor)
            presenter.addPresentation(PatchPresentation(patch, frame))
        }
    }

    fun colorBar(coloret: Coloret, widthlet: Sizelet): Span0 {
        return Span0.create { presenter ->
            val bar = presenter.device
            val width = widthlet.toFloat(presenter.human, bar.relatedWidth)
            val frame = Frame(width, bar.fixedHeight, bar.elevation)
            val patch = bar.addPatch(frame, RectangleShape(), coloret.toArgb())
            presenter.addPresentation(PatchPresentation(patch, frame))
        }
    }

    fun textColumn(textString: String, textStylet: TextStylet): Div0 {
        return textTile(textString, textStylet).toColumn()
    }

    fun gapColumn(heightlet: Sizelet): Div0 {
        return colorColumn(heightlet, null)
    }

    fun colorColumn(heightlet: Sizelet, coloret: Coloret?): Div0 {
        return Div0.create { presenter ->
            val pole = presenter.device
            val height = heightlet.toFloat(presenter.human, pole.relatedHeight)
            val frame = Frame(pole.fixedWidth, height, pole.elevation)
            val patch = if (coloret == null)
                null
            else
                pole.addPatch(frame, RectangleShape(), coloret.toArgb())
            val presentation = object : BooleanPresentation() {

                override fun getWidth(): Float {
                    return pole.fixedWidth
                }

                override fun getHeight(): Float {
                    return height
                }

                override fun onCancel() {
                    patch?.remove()
                }
            }
            presenter.addPresentation(presentation)
        }
    }

    val moreIndicator: Div0 by lazy {
        val moreMarker = textTile("▼", TextStylet.IMPORTANT_DARK)
        val moreMarkerInset = .01f
        val leftMarker = moreMarker.toColumn(moreMarkerInset)
        val rightMarker = moreMarker.toColumn(1f - moreMarkerInset)
        val moreIndicator = leftMarker.placeBefore(rightMarker, 0)
        moreIndicator
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
                                ?.expandDown(colorColumn(Sizelet.readables(.2f), com.rubyhuntersky.coloret.Coloret.MAGENTA))
                                ?.expandDown(paddedItem)
                                ?: paddedItem
                        index++
                    }
                    menu = menu
                            ?.padVertical(Sizelet.READABLE)
                            ?.placeBefore(colorColumn(Sizelet.PREVIOUS, com.rubyhuntersky.coloret.Coloret.WHITE), 1)
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
}
