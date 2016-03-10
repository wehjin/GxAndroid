package com.rubyhuntersky.gx.uis.tiles.operations;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.uis.tiles.Tile0;
import com.rubyhuntersky.gx.uis.tiles.Tile1;
import com.rubyhuntersky.gx.uis.tiles.Tile2;

/**
 * @author wehjin
 * @since 2/14/16.
 */

abstract public class TileOperation1 {

    abstract public Tile0 apply0(final Tile0 base, final Tile0 expansion);

    public <C> Tile1<C> apply0(final Tile1<C> base, final Tile0 expansion) {
        return Tile1.create(new Tile1.OnBind<C>() {
            @NonNull
            @Override
            public Tile0 onBind(C condition) {
                return TileOperation1.this.apply0(base.bind(condition), expansion);
            }
        });
    }

    public <C1, C2> Tile2<C1, C2> apply0(final Tile2<C1, C2> base, final Tile0 expansion) {
        return Tile2.create(new Tile2.OnBind<C1, C2>() {
            @NonNull
            @Override
            public Tile1<C2> onBind(C1 condition) {
                return TileOperation1.this.apply0(base.bind(condition), expansion);
            }
        });
    }

    public <C> Tile1<C> apply1(final Tile0 base, final Tile1<C> expansion) {
        return Tile1.create(new Tile1.OnBind<C>() {
            @NonNull
            @Override
            public Tile0 onBind(C condition) {
                return TileOperation1.this.apply0(base, expansion.bind(condition));
            }
        });
    }

    public <C1, C2> Tile2<C1, C2> apply1(final Tile1<C1> base, final Tile1<C2> expansion) {
        return Tile2.create(new Tile2.OnBind<C1, C2>() {
            @NonNull
            @Override
            public Tile1<C2> onBind(C1 condition) {
                return TileOperation1.this.apply1(base.bind(condition), expansion);
            }
        });
    }
}

