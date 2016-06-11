package com.rubyhuntersky.gx

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.rubyhuntersky.coloret.Coloret
import com.rubyhuntersky.gx.basics.Frame
import com.rubyhuntersky.gx.basics.Sizelet
import com.rubyhuntersky.gx.basics.TextStylet
import com.rubyhuntersky.gx.internal.patches.Patch
import com.rubyhuntersky.gx.internal.presenters.SwitchDivPresenter
import com.rubyhuntersky.gx.internal.shapes.RectangleShape
import com.rubyhuntersky.gx.internal.shapes.ViewShape
import com.rubyhuntersky.gx.presentations.PatchPresentation
import com.rubyhuntersky.gx.reactions.Reaction
import com.rubyhuntersky.gx.reactions.TapReaction
import com.rubyhuntersky.gx.uis.divs.Div
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

    fun <UnboundDiv> printReadEvaluate(repl: Div.PrintReadEvaluater<UnboundDiv>, unbound: UnboundDiv): Div0 {
        return Div0.create(object : Div.OnPresent {

            override fun onPresent(presenter: Div.Presenter) {
                val human = presenter.human
                val pole = presenter.pole
                val switchPresenter = SwitchDivPresenter(human, pole, presenter)
                presenter.addPresentation(switchPresenter)
                present(repl, switchPresenter)
            }

            internal fun present(repl: Div.PrintReadEvaluater<UnboundDiv>, switchPresenter: SwitchDivPresenter) {
                if (switchPresenter.isCancelled)
                    return
                val bound = repl.print(unbound)
                val human = switchPresenter.human
                val pole = switchPresenter.device
                switchPresenter.setPresentation(bound.present(human, pole, object : Div.ForwardingObserver(switchPresenter) {
                    override fun onReaction(reaction: Reaction) {
                        if (switchPresenter.isCancelled)
                            return
                        repl.read(reaction)
                        if (repl.eval()) {
                            present(repl, switchPresenter)
                        }
                    }
                }))
            }
        })
    }


    fun textColumn(textString: String, textStylet: TextStylet): Div0 {
        return textTile(textString, textStylet).toColumn()
    }

    fun gapColumn(heightlet: Sizelet): Div0 {
        return colorColumn(heightlet, null)
    }

    fun colorColumn(heightlet: Sizelet, coloret: Coloret?): Div0 {
        val onPresent: Div.OnPresent = object : Div.OnPresent {
            override fun onPresent(presenter: Div.Presenter) {
                presenter.addPresentation(object : Div.PresenterPresentation(presenter) {

                    var patch: Patch? = null

                    init {
                        val height = heightlet.toFloat(human, pole.relatedHeight)
                        val frame = Frame(pole.fixedWidth, height, pole.elevation)
                        patch = if (coloret == null)
                            null
                        else
                            pole.addPatch(frame, RectangleShape(), coloret.toArgb())
                        presenter.onHeight(height)
                    }

                    override fun onCancel() {
                        patch?.remove()
                    }
                })
            }
        }
        return Div0.create(onPresent)
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
        return Div0.create(object : Div.OnPresent {
            override fun onPresent(presenter: Div.Presenter) {
                object : Div.PresenterPresentation(presenter) {
                    var launcherPresentation: Div.Presentation? = null
                    var menuPresentation: Div.Presentation? = null

                    init {
                        presentLauncher(startIndex)
                    }

                    override fun onCancel() {
                        menuPresentation?.cancel()
                        launcherPresentation?.cancel()
                    }

                    fun presentLauncher(index: Int) {
                        val item = items[index].padVertical(Sizelet.QUARTER_FINGER)
                        val launcher = item.placeBefore(moreIndicator, 1, .5f).enableTap("launcher")
                        launcherPresentation?.cancel()
                        launcherPresentation = launcher.present(human, pole, object : Div.ForwardingObserver(presenter) {
                            override fun onReaction(reaction: Reaction) {
                                if (reaction is TapReaction<*>) {
                                    presentMenu(index)
                                }
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
                        menuPresentation = pole.present(menu, object : Div.ForwardingObserver(presenter) {
                            override fun onHeight(height: Float) {
                                // Do nothing.  Menu height is not the dropdown height.
                            }

                            override fun onReaction(reaction: Reaction) {
                                if (reaction is TapReaction<*>) {
                                    menuPresentation?.cancel()
                                    presentLauncher(reaction.tag as Int)
                                }
                            }
                        })
                    }
                }
            }
        })
    }
}
