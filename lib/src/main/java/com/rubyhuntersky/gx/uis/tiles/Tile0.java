package com.rubyhuntersky.gx.uis.tiles;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.basics.Sizelet;
import com.rubyhuntersky.gx.devices.bars.Bar;
import com.rubyhuntersky.gx.devices.mosaics.Mosaic;
import com.rubyhuntersky.gx.devices.mosaics.ShiftMosaic;
import com.rubyhuntersky.gx.internal.interchange.ToColumnOperation;
import com.rubyhuntersky.gx.internal.presenters.BasePresenter;
import com.rubyhuntersky.gx.uis.OnPresent;
import com.rubyhuntersky.gx.internal.presenters.Presenter;
import com.rubyhuntersky.gx.observers.Observer;
import com.rubyhuntersky.gx.presentations.Presentation;
import com.rubyhuntersky.gx.uis.core.Ui0;
import com.rubyhuntersky.gx.uis.divs.Div0;
import com.rubyhuntersky.gx.uis.spans.Span0;
import com.rubyhuntersky.gx.uis.tiles.operations.ExpandDownTileOperation1;
import com.rubyhuntersky.gx.uis.tiles.operations.ExpandHorizontalTileOperation0;
import com.rubyhuntersky.gx.uis.tiles.operations.ExpandLeftTileOperation1;
import com.rubyhuntersky.gx.uis.tiles.operations.ExpandVerticalTileOperation0;
import com.rubyhuntersky.gx.uis.tiles.operations.NameTileOperation0;

/**
 * @author wehjin
 * @since 1/28/16.
 */

abstract public class Tile0 implements Ui0<Mosaic> {

    abstract public Presentation present(Human human, Mosaic mosaic, Observer observer);

    public Tile0 name(final String name) {
        return new NameTileOperation0(name).apply(this);
    }

    public Tile0 expandLeft(final Tile0 expansion) {
        return new ExpandLeftTileOperation1().apply0(this, expansion);
    }

    public Tile0 expandDown(final Tile0 expansion) {
        return new ExpandDownTileOperation1().apply0(this, expansion);
    }

    public <C> Tile1<C> expandDown(final Tile1<C> expansion) {
        return new ExpandDownTileOperation1().apply1(this, expansion);
    }

    public Tile0 expandVertical(final Sizelet padlet) {
        return new ExpandVerticalTileOperation0(padlet).apply(this);
    }

    public Tile0 expandHorizontal(final Sizelet padlet) {
        return new ExpandHorizontalTileOperation0(padlet).apply(this);
    }

    public Span0 toBar() {
        return Span0.create(new OnPresent<Bar>() {
            @Override
            public void onPresent(Presenter<Bar> presenter) {
                final Bar bar = presenter.getDevice();
                final Mosaic mosaic = new Mosaic(bar.relatedWidth, bar.fixedHeight, bar.elevation, bar);
                final ShiftMosaic frameShiftTile = mosaic.withShift();
                final Presentation presentation = present(presenter.getHuman(), frameShiftTile, presenter);
                final float presentationHeight = presentation.getHeight();
                final float extraHeight = bar.fixedHeight - presentationHeight;
                final float anchor = .5f;
                frameShiftTile.doShift(0, extraHeight * anchor);
                presenter.addPresentation(presentation);
            }
        });
    }

    public Div0 toColumn() {
        return new ToColumnOperation().applyTo(this);
    }

    public static Tile0 create(final OnPresent<Mosaic> onPresent) {
        return new Tile0() {
            @Override
            public Presentation present(Human human, final Mosaic mosaic, Observer observer) {
                final BasePresenter<Mosaic> presenter = new BasePresenter<Mosaic>(human, mosaic, observer) {
                    @Override
                    public float getWidth() {
                        float union = 0;
                        for (Presentation presentation : presentations) {
                            union = Math.max(union, presentation.getWidth());
                        }
                        return union;
                    }

                    @Override
                    public float getHeight() {
                        float union = 0;
                        for (Presentation presentation : presentations) {
                            union = Math.max(union, presentation.getHeight());
                        }
                        return union;
                    }
                };
                onPresent.onPresent(presenter);
                return presenter;
            }
        };
    }
}
