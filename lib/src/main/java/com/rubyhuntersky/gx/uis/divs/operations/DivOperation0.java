package com.rubyhuntersky.gx.uis.divs.operations;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.uis.divs.Div0;
import com.rubyhuntersky.gx.uis.divs.Div1;
import com.rubyhuntersky.gx.uis.divs.Div2;
import com.rubyhuntersky.gx.uis.divs.Div3;
import com.rubyhuntersky.gx.uis.divs.Div4;
import com.rubyhuntersky.gx.uis.divs.Div5;

/**
 * @author wehjin
 * @since 2/9/16.
 */
abstract public class DivOperation0 {

    abstract public Div0 apply(final Div0 base);

    public <C> Div1<C> apply(final Div1<C> base) {
        return Div1.create(new Div1.OnBind<C>() {
            @NonNull
            @Override
            public Div0 onBind(C condition) {
                return DivOperation0.this.apply(base.bind(condition));
            }
        });
    }

    public <C1, C2> Div2<C1, C2> apply(final Div2<C1, C2> base) {
        return Div2.create(new Div2.OnBind<C1, C2>() {
            @Override
            public Div1<C2> onBind(C1 condition) {
                return DivOperation0.this.apply(base.bind(condition));
            }
        });
    }

    public <C1, C2, C3> Div3<C1, C2, C3> apply(final Div3<C1, C2, C3> base) {
        return Div3.create(new Div3.OnBind<C1, C2, C3>() {
            @Override
            public Div2<C2, C3> onBind(C1 condition) {
                return DivOperation0.this.apply(base.bind(condition));
            }
        });
    }

    public <C1, C2, C3, C4> Div4<C1, C2, C3, C4> apply(final Div4<C1, C2, C3, C4> base) {
        return Div4.create(new Div4.OnBind<C1, C2, C3, C4>() {
            @Override
            public Div3<C2, C3, C4> onBind(C1 condition) {
                return DivOperation0.this.apply(base.bind(condition));
            }
        });
    }

    public <C1, C2, C3, C4, C5> Div5<C1, C2, C3, C4, C5> apply(final Div5<C1, C2, C3, C4, C5> previous) {
        return Div5.create(new Div5.OnBind<C1, C2, C3, C4, C5>() {
            @Override
            public Div4<C2, C3, C4, C5> onBind(C1 condition) {
                return DivOperation0.this.apply(previous.bind(condition));
            }
        });
    }
}
