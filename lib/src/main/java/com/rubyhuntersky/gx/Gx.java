package com.rubyhuntersky.gx;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.rubyhuntersky.coloret.Coloret;
import com.rubyhuntersky.gx.basics.Frame;
import com.rubyhuntersky.gx.basics.Sizelet;
import com.rubyhuntersky.gx.basics.TextHeight;
import com.rubyhuntersky.gx.basics.TextSize;
import com.rubyhuntersky.gx.basics.TextStyle;
import com.rubyhuntersky.gx.basics.TextStylet;
import com.rubyhuntersky.gx.devices.bars.Bar;
import com.rubyhuntersky.gx.devices.mosaics.Mosaic;
import com.rubyhuntersky.gx.devices.poles.Pole;
import com.rubyhuntersky.gx.internal.patches.Patch;
import com.rubyhuntersky.gx.internal.presenters.Presenter;
import com.rubyhuntersky.gx.internal.shapes.RectangleShape;
import com.rubyhuntersky.gx.internal.shapes.ViewShape;
import com.rubyhuntersky.gx.presentations.BooleanPresentation;
import com.rubyhuntersky.gx.presentations.PatchPresentation;
import com.rubyhuntersky.gx.presentations.Presentation;
import com.rubyhuntersky.gx.uis.OnPresent;
import com.rubyhuntersky.gx.uis.divs.Div0;
import com.rubyhuntersky.gx.uis.spans.Span0;
import com.rubyhuntersky.gx.uis.tiles.Tile0;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public class Gx {


    public static final String TAG = Gx.class.getSimpleName();

    static public Tile0 textTile(final String textString, final TextStylet textStylet) {
        return Tile0.create(new OnPresent<Mosaic>() {
            @Override
            public void onPresent(Presenter<Mosaic> presenter) {
                final Human human = presenter.getHuman();
                final Mosaic mosaic = presenter.getDevice();
                final TextStyle textStyle = textStylet.toStyle(human, mosaic.relatedHeight);
                final TextSize textSize = mosaic.measureText(textString, textStyle);
                final Frame frame = new Frame(textSize.textWidth, textSize.textHeight.height, mosaic.elevation);
                final TextHeight textHeight = textSize.textHeight;
                final Frame textFrame = frame.withVerticalShift(-textHeight.topPadding)
                      .withVerticalLength(textHeight.topPadding + 1.5f * textHeight.height);
                final ViewShape viewShape = new ViewShape() {
                    @Override
                    public View createView(Context context) {
                        final TextView textView = new TextView(context);
                        textView.setGravity(Gravity.TOP);
                        textView.setSingleLine();
                        textView.setTextColor(textStyle.typecolor);
                        textView.setTypeface(textStyle.typeface);
                        textView.setTextSize(textStyle.typeheight);
                        textView.setText(textString);
                        textView.setIncludeFontPadding(false);
                        textView.setContentDescription("TextTile");
                        return textView;
                    }
                };
                final Patch patch = mosaic.addPatch(textFrame, viewShape, textStyle.typecolor);
                presenter.addPresentation(new PatchPresentation(patch, frame));
            }
        });
    }

    static public Span0 colorBar(final Coloret coloret, final Sizelet widthlet) {
        return Span0.create(new OnPresent<Bar>() {
            @Override
            public void onPresent(Presenter<Bar> presenter) {
                final Bar bar = presenter.getDevice();
                final float width = widthlet.toFloat(presenter.getHuman(), bar.relatedWidth);
                final Frame frame = new Frame(width, bar.fixedHeight, bar.elevation);
                final Patch patch = bar.addPatch(frame, new RectangleShape(), coloret.toArgb());
                presenter.addPresentation(new PatchPresentation(patch, frame));
            }
        });
    }

    static public Div0 textColumn(final String textString, final TextStylet textStylet) {
        return textTile(textString, textStylet).toColumn();
    }

    static public Div0 gapColumn(final Sizelet heightlet) {
        return colorColumn(heightlet, null);
    }

    static public Div0 colorColumn(final Sizelet heightlet, @Nullable final Coloret coloret) {
        return Div0.create(new OnPresent<Pole>() {
            @Override
            public void onPresent(Presenter<Pole> presenter) {
                final Pole pole = presenter.getDevice();
                final float height = heightlet.toFloat(presenter.getHuman(), pole.getRelatedHeight());
                final Frame frame = new Frame(pole.getFixedWidth(), height, pole.getElevation());
                final Patch patch = coloret == null
                      ? null
                      : pole.addPatch(frame, new RectangleShape(), coloret.toArgb());
                final Presentation presentation = new BooleanPresentation() {

                    @Override
                    public float getWidth() {
                        return pole.getFixedWidth();
                    }

                    @Override
                    public float getHeight() {
                        return height;
                    }

                    @Override
                    protected void onCancel() {
                        if (patch != null) {
                            patch.remove();
                        }
                    }
                };
                presenter.addPresentation(presentation);
            }
        });
    }
}
