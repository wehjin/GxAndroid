package com.rubyhuntersky.gx.uis.divs;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.basics.Sizelet;
import com.rubyhuntersky.gx.uis.divs.operations.ExpandDownDivOperation1;
import com.rubyhuntersky.gx.uis.divs.operations.ExpandVerticalDivOperation0;

/**
 * @author wehjin
 * @since 1/30/16.
 */

abstract public class Div1<C> {

    public abstract Div0 bind(C condition);

    private Div1() {
    }

    public static <C> Div1<C> create(final OnBind<C> onBind) {
        return new Div1<C>() {
            @Override
            public Div0 bind(C condition) {
                return onBind.onBind(condition);
            }
        };
    }

    public Div1<C> padBottom(final Sizelet padlet) {
        // TODO Test
        final Div1<C> ui1 = this;
        return Div1.create(new OnBind<C>() {
            @NonNull
            @Override
            public Div0 onBind(final C condition) {
                return ui1.bind(condition).padBottom(padlet);
            }
        });
    }

    public Div1<C> padHorizontal(final Sizelet padlet) {
        // TODO Test
        return Div1.create(new OnBind<C>() {
            @NonNull
            @Override
            public Div0 onBind(final C condition) {
                return Div1.this.bind(condition).padHorizontal(padlet);
            }
        });
    }


    public Div1<C> placeBefore(final Div0 div0, final int elevate) {
        return Div1.create(new OnBind<C>() {
            @NonNull
            @Override
            public Div0 onBind(final C condition) {
                return Div1.this.bind(condition).placeBefore(div0, elevate);
            }
        });
    }

    public Div2<C, Div0> expandDown() {
        return new ExpandDownDivOperation1().applyFuture0(this);
    }

    public Div1<C> expandDown(final Div0 expansion) {
        return new ExpandDownDivOperation1().apply0(this, expansion);
    }

    public <C2> Div2<C, C2> expandDown(final Div1<C2> expansion) {
        return new ExpandDownDivOperation1().apply1(this, expansion);
    }

    public <C2, C3> Div3<C, C2, C3> expandDown(final Div2<C2, C3> expansion) {
        return new ExpandDownDivOperation1().apply2(this, expansion);
    }

    public <C2, C3, C4> Div4<C, C2, C3, C4> expandDown(final Div3<C2, C3, C4> expansion) {
        return new ExpandDownDivOperation1().apply3(this, expansion);
    }

    public Div1<C> expandVertical(final Sizelet heightlet) {
        return new ExpandVerticalDivOperation0(heightlet).apply(this);
    }

    public interface OnBind<C> {
        @NonNull
        Div0 onBind(C condition);
    }

}

