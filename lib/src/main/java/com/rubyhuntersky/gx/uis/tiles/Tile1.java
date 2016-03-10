package com.rubyhuntersky.gx.uis.tiles;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.devices.mosaics.Mosaic;
import com.rubyhuntersky.gx.uis.divs.Div1;
import com.rubyhuntersky.gx.internal.interchange.ToColumnOperation;
import com.rubyhuntersky.gx.uis.tiles.operations.ExpandDownTileOperation1;
import com.rubyhuntersky.gx.uis.tiles.operations.ExpandLeftTileOperation1;
import com.rubyhuntersky.gx.uis.tiles.operations.NameTileOperation0;
import com.rubyhuntersky.gx.uis.core.Ui1;

/**
 * @author wehjin
 * @since 1/30/16.
 */

abstract public class Tile1<C> implements Ui1<Mosaic, C> {

    private Tile1() {
    }

    @Override
    public abstract Tile0 bind(C condition);

    public Tile1<C> name(final String name) {
        return new NameTileOperation0(name).apply(this);
    }

    public Tile1<C> expandLeft(final Tile0 expansion) {
        return new ExpandLeftTileOperation1().apply0(this, expansion);
    }

    public Tile1<C> expandDown(final Tile0 expansion) {
        return new ExpandDownTileOperation1().apply0(this, expansion);
    }

    public <C2> Tile2<C, C2> expandDown(final Tile1<C2> expansion) {
        return new ExpandDownTileOperation1().apply1(this, expansion);
    }

    public Div1<C> toColumn() {
        return new ToColumnOperation().applyTo(this);
    }

    public static <C> Tile1<C> create(final OnBind<C> onBind) {
        return new Tile1<C>() {

            @Override
            public Tile0 bind(C condition) {
                return onBind.onBind(condition);
            }
        };
    }

    public interface OnBind<C> {
        @NonNull
        Tile0 onBind(C condition);
    }
}
