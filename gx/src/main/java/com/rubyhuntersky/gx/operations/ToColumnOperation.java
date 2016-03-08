package com.rubyhuntersky.gx.operations;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.columns.Column;
import com.rubyhuntersky.gx.columns.Div0;
import com.rubyhuntersky.gx.columns.Div1;
import com.rubyhuntersky.gx.columns.Div2;
import com.rubyhuntersky.gx.presentations.Presentation;
import com.rubyhuntersky.gx.presentations.ResizePresentation;
import com.rubyhuntersky.gx.presenters.OnPresent;
import com.rubyhuntersky.gx.presenters.Presenter;
import com.rubyhuntersky.gx.tiles.Mosaic;
import com.rubyhuntersky.gx.tiles.ShiftMosaic;
import com.rubyhuntersky.gx.tiles.Tile0;
import com.rubyhuntersky.gx.tiles.Tile1;
import com.rubyhuntersky.gx.tiles.Tile2;

/**
 * @author wehjin
 * @since 2/9/16.
 */

public class ToColumnOperation {

    public Div0 applyTo(final Tile0 tile0) {
        return Div0.create(new OnPresent<Column>() {
            @Override
            public void onPresent(Presenter<Column> presenter) {
                Column column = presenter.getDisplay();
                final Mosaic mosaic = new Mosaic(column.fixedWidth, column.relatedHeight, column.elevation, column);
                final ShiftMosaic frameShiftTile = mosaic.withShift();
                final Presentation presentation = tile0.present(presenter.getHuman(), frameShiftTile, presenter);
                final float presentationWidth = presentation.getWidth();
                final float extraWidth = column.fixedWidth - presentationWidth;
                final float anchor = .5f;
                frameShiftTile.setShift(extraWidth * anchor, 0);
                presenter.addPresentation(new ResizePresentation(column.fixedWidth,
                                                                 presentation.getHeight(),
                                                                 presentation));
            }
        });
    }

    public <C> Div1<C> applyTo(final Tile1<C> tile1) {
        return Div1.create(new Div1.OnBind<C>() {
            @NonNull
            @Override
            public Div0 onBind(final C condition) {
                return ToColumnOperation.this.applyTo(tile1.bind(condition));
            }
        });
    }

    public <C1, C2> Div2<C1, C2> applyTo(final Tile2<C1, C2> tile) {
        return Div2.create(new Div2.OnBind<C1, C2>() {
            @NonNull
            @Override
            public Div1<C2> onBind(final C1 condition) {
                return ToColumnOperation.this.applyTo(tile.bind(condition));
            }
        });
    }
}
