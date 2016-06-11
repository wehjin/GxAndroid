package com.rubyhuntersky.gx.internal.interchange;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.devices.mosaics.Mosaic;
import com.rubyhuntersky.gx.devices.mosaics.ShiftMosaic;
import com.rubyhuntersky.gx.devices.poles.Pole;
import com.rubyhuntersky.gx.presentations.Presentation;
import com.rubyhuntersky.gx.uis.divs.Div;
import com.rubyhuntersky.gx.uis.divs.Div0;
import com.rubyhuntersky.gx.uis.divs.Div1;
import com.rubyhuntersky.gx.uis.divs.Div2;
import com.rubyhuntersky.gx.uis.tiles.Tile0;
import com.rubyhuntersky.gx.uis.tiles.Tile1;
import com.rubyhuntersky.gx.uis.tiles.Tile2;

/**
 * @author wehjin
 * @since 2/9/16.
 */

public class ToColumnOperation {

    public Div0 applyTo(final Tile0 tile0, final float anchor) {
        return Div0.create(new Div.OnPresent() {
            @Override
            public void onPresent(@NonNull final Div.Presenter presenter) {
                presenter.addPresentation(new Div.PresenterPresentation(presenter) {

                    private Presentation presentation;

                    {
                        final Human human = getHuman();
                        final Pole pole = getPole();
                        final Mosaic mosaic = new Mosaic(pole.getFixedWidth(),
                                                         pole.getRelatedHeight(),
                                                         pole.getElevation(),
                                                         pole);
                        final ShiftMosaic shiftMosaic = mosaic.withShift();
                        presentation = tile0.present(human, shiftMosaic, presenter);
                        final float extraWidth = pole.getFixedWidth() - presentation.getWidth();
                        shiftMosaic.doShift(extraWidth * anchor, 0);
                        presenter.onHeight(presentation.getHeight());
                    }

                    @Override
                    public void onCancel() {
                        presentation.cancel();
                    }
                });
            }
        });
    }

    public Div0 applyTo(final Tile0 tile0) {
        return applyTo(tile0, .5f);
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
