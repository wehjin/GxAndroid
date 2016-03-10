package com.rubyhuntersky.gx.uis.tiles.operations;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.devices.mosaics.Mosaic;
import com.rubyhuntersky.gx.devices.mosaics.ShiftMosaic;
import com.rubyhuntersky.gx.uis.OnPresent;
import com.rubyhuntersky.gx.internal.presenters.Presenter;
import com.rubyhuntersky.gx.presentations.Presentation;
import com.rubyhuntersky.gx.presentations.ResizePresentation;
import com.rubyhuntersky.gx.uis.tiles.Tile0;

/**
 * @author wehjin
 * @since 2/14/16.
 */

public class ExpandLeftTileOperation1 extends TileOperation1 {
    @Override
    public Tile0 apply0(final Tile0 base, final Tile0 expansion) {
        return Tile0.create(new OnPresent<Mosaic>() {
            @Override
            public void onPresent(Presenter<Mosaic> presenter) {
                final Human human = presenter.getHuman();
                final Mosaic mosaic = presenter.getDevice();
                final ShiftMosaic baseShift = mosaic.withShift();
                final ShiftMosaic expansionShift = mosaic.withShift();
                final Presentation presentBase = base.present(human, baseShift, presenter);
                final Presentation presentExpansion = expansion.present(human, expansionShift, presenter);
                final float height = Math.max(presentBase.getHeight(), presentExpansion.getHeight());
                baseShift.doShift(presentExpansion.getWidth(), (height - presentBase.getHeight()) * .5f);
                expansionShift.doShift(0, (height - presentExpansion.getHeight()) * .5f);
                presenter.addPresentation(presentExpansion);
                presenter.addPresentation(new ResizePresentation(presentExpansion.getWidth() + presentBase.getWidth(),
                                                                 height,
                                                                 presentBase));
            }
        });
    }
}
