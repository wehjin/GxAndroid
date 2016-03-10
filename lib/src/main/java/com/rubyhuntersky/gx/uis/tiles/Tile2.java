package com.rubyhuntersky.gx.uis.tiles;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.devices.mosaics.Mosaic;
import com.rubyhuntersky.gx.uis.divs.Div2;
import com.rubyhuntersky.gx.internal.interchange.ToColumnOperation;
import com.rubyhuntersky.gx.uis.tiles.operations.ExpandDownTileOperation1;
import com.rubyhuntersky.gx.uis.tiles.operations.ExpandLeftTileOperation1;
import com.rubyhuntersky.gx.uis.tiles.operations.NameTileOperation0;
import com.rubyhuntersky.gx.uis.core.Ui2;

/**
 * @author wehjin
 * @since 1/30/16.
 */

abstract public class Tile2<C1, C2> implements Ui2<Mosaic, C1, C2> {

    private Tile2() {
    }

    @Override
    public abstract Tile1<C2> bind(C1 condition);

    public Tile2<C1, C2> name(final String name) {
        return new NameTileOperation0(name).apply(this);
    }

    public Tile2<C1, C2> expandLeft(final Tile0 expansion) {
        return new ExpandLeftTileOperation1().apply0(this, expansion);
    }

    public Tile2<C1, C2> expandDown(final Tile0 expansion) {
        return new ExpandDownTileOperation1().apply0(this, expansion);
    }

    public Div2<C1, C2> toColumn() {
        return new ToColumnOperation().applyTo(this);
    }

    public static <C1, C2> Tile2<C1, C2> create(final OnBind<C1, C2> onBind) {
        return new Tile2<C1, C2>() {

            @Override
            public Tile1<C2> bind(C1 condition) {
                return onBind.onBind(condition);
            }
        };
    }

    public interface OnBind<C1, C2> {
        @NonNull
        Tile1<C2> onBind(C1 condition);
    }
}
