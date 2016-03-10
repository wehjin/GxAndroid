package com.rubyhuntersky.gx.android;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.basics.Frame;
import com.rubyhuntersky.gx.basics.ShapeSize;
import com.rubyhuntersky.gx.basics.TextStylet;
import com.rubyhuntersky.gx.devices.mosaics.Mosaic;
import com.rubyhuntersky.gx.internal.patches.Patch;
import com.rubyhuntersky.gx.uis.OnPresent;
import com.rubyhuntersky.gx.internal.presenters.Presenter;
import com.rubyhuntersky.gx.internal.shapes.SpinnerViewShape;
import com.rubyhuntersky.gx.presentations.PatchPresentation;
import com.rubyhuntersky.gx.uis.divs.Div0;
import com.rubyhuntersky.gx.uis.spans.Span0;
import com.rubyhuntersky.gx.uis.spans.Span1;
import com.rubyhuntersky.gx.uis.tiles.Tile0;
import com.rubyhuntersky.gx.uis.tiles.Tile1;
import com.rubyhuntersky.gx.uis.tiles.TileCreator;

import java.util.List;

/**
 * @author wehjin
 * @since 1/27/16.
 */

public class AndroidGx {

    public static <C> Tile0 spinnerTile(final Tile1<C> optionsTile, final List<C> options, final int selectedOption) {
        return Tile0.create(new OnPresent<Mosaic>() {
            @Override
            public void onPresent(Presenter<Mosaic> presenter) {
                final Mosaic mosaic = presenter.getDevice();
                final SpinnerViewShape shape = new SpinnerViewShape<>(options, optionsTile, selectedOption, presenter);
                final ShapeSize shapeSize = mosaic.measureShape(shape);
                final Frame frame = new Frame(shapeSize.measuredWidth, shapeSize.measuredHeight, mosaic.elevation);
                final Patch patch = mosaic.addPatch(frame, shape, 0);
                presenter.addPresentation(new PatchPresentation(patch, frame));
            }
        });
    }

    public static <C> Tile1<Integer> spinnerTile(final Tile1<C> optionsTile, final List<C> options) {
        return Tile1.create(new Tile1.OnBind<Integer>() {
            @NonNull
            @Override
            public Tile0 onBind(Integer condition) {
                return spinnerTile(optionsTile, options, condition);
            }
        });
    }

    public static Tile0 spinnerTile(final List<String> options, final int selectedOption) {
        return spinnerTile(TileCreator.textTile1(TextStylet.IMPORTANT_DARK), options, selectedOption);
    }

    public static Tile1<Integer> spinnerTile(final List<String> options) {
        return spinnerTile(TileCreator.textTile1(TextStylet.IMPORTANT_DARK), options);
    }

    public static Div0 spinnerColumn(final List<String> options, final int selectedOption) {
        return spinnerTile(options, selectedOption).toColumn();
    }

    public static Span0 spinnerBar(final List<String> options, final int selectedOption) {
        return spinnerTile(options, selectedOption).toBar();
    }

    public static Span1<Integer> spinnerBar(final List<String> options) {
        return Span1.create(new Span1.OnBind<Integer>() {
            @Override
            public Span0 onBind(Integer condition) {
                return spinnerBar(options, condition);
            }
        });
    }

}
