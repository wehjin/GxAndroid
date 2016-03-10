package com.rubyhuntersky.gx.uis.spans;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.devices.bars.Bar;
import com.rubyhuntersky.gx.basics.Sizelet;
import com.rubyhuntersky.gx.uis.divs.Div0;
import com.rubyhuntersky.gx.uis.divs.Div1;
import com.rubyhuntersky.gx.uis.tiles.Tile0;
import com.rubyhuntersky.gx.uis.core.Ui1;

/**
 * @author wehjin
 * @since 1/30/16.
 */

public abstract class Span1<C> implements Ui1<Bar, C> {

    public abstract Span0 bind(C condition);

    public static <C> Span1<C> create(final OnBind<C> onBind) {
        return new Span1<C>() {
            @Override
            public Span0 bind(C condition) {
                return onBind.onBind(condition);
            }
        };
    }

    public Span1<C> expandStart(final Tile0 tile0) {
        return Span1.create(new OnBind<C>() {
            @Override
            public Span0 onBind(C condition) {
                return Span1.this.bind(condition).expandStart(tile0);
            }
        });
    }

    public Div1<C> toColumn(final Sizelet heightlet) {
        return Div1.create(new Div1.OnBind<C>() {
            @NonNull
            @Override
            public Div0 onBind(final C condition) {
                return Span1.this.bind(condition).toColumn(heightlet);
            }
        });
    }

    public interface OnBind<C> {
        Span0 onBind(C condition);
    }
}
