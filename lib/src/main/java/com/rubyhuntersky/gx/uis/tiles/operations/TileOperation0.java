package com.rubyhuntersky.gx.uis.tiles.operations;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.uis.tiles.Tile0;
import com.rubyhuntersky.gx.uis.tiles.Tile1;
import com.rubyhuntersky.gx.uis.tiles.Tile2;

/**
 * @author wehjin
 * @since 2/14/16.
 */

abstract public class TileOperation0 {

    abstract public Tile0 apply(final Tile0 base);

    public <C> Tile1<C> apply(final Tile1<C> base) {
        return Tile1.create(new Tile1.OnBind<C>() {
            @NonNull
            @Override
            public Tile0 onBind(C condition) {
                return TileOperation0.this.apply(base.bind(condition));
            }
        });
    }

    public <C1, C2> Tile2<C1, C2> apply(final Tile2<C1, C2> base) {
        return Tile2.create(new Tile2.OnBind<C1, C2>() {
            @NonNull
            @Override
            public Tile1<C2> onBind(C1 condition) {
                return TileOperation0.this.apply(base.bind(condition));
            }
        });
    }
}

