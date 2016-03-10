package com.rubyhuntersky.gx.tiles.operations;

import com.rubyhuntersky.gx.client.Human;
import com.rubyhuntersky.gx.internal.presentations.BooleanPresentation;
import com.rubyhuntersky.gx.client.Presentation;
import com.rubyhuntersky.gx.internal.presenters.OnPresent;
import com.rubyhuntersky.gx.internal.presenters.Presenter;
import com.rubyhuntersky.gx.tiles.Mosaic;
import com.rubyhuntersky.gx.tiles.ShiftMosaic;
import com.rubyhuntersky.gx.tiles.Tile0;

/**
 * @author wehjin
 * @since 2/14/16.
 */

public class ExpandDownTileOperation1 extends TileOperation1 {
    @Override
    public Tile0 apply0(final Tile0 base, final Tile0 expansion) {
        return Tile0.create(new OnPresent<Mosaic>() {
            @Override
            public void onPresent(Presenter<Mosaic> presenter) {
                final Human human = presenter.getHuman();
                final Mosaic mosaic = presenter.getDevice();
                final ShiftMosaic baseMosaic = mosaic.withShift();
                final ShiftMosaic expansionMosaic = mosaic.withShift();
                final Presentation basePresentation = base.present(human, baseMosaic, presenter);
                final Presentation expansionPresentation = expansion.present(human, expansionMosaic, presenter);
                final float width = Math.max(basePresentation.getWidth(), expansionPresentation.getWidth());
                final float baseShiftX = (width - basePresentation.getWidth()) * .5f;
                final float expansionShiftX = (width - expansionPresentation.getWidth()) * .5f;
                baseMosaic.setShift(baseShiftX, 0);
                expansionMosaic.setShift(expansionShiftX, basePresentation.getHeight());
                presenter.addPresentation(new BooleanPresentation() {
                    @Override
                    protected void onCancel() {
                        basePresentation.cancel();
                        expansionPresentation.cancel();
                    }

                    @Override
                    public float getWidth() {
                        return width;
                    }

                    @Override
                    public float getHeight() {
                        return basePresentation.getHeight() + expansionPresentation.getHeight();
                    }
                });
            }
        });
    }
}
