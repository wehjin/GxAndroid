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
 * @since 2/16/16.
 */

abstract public class DivOperation1 {

    abstract public Div0 apply0(final Div0 base, final Div0 expansion);

    public <C> Div1<C> apply0(final Div1<C> base, final Div0 expansion) {
        return Div1.create(new Div1.OnBind<C>() {
            @NonNull
            @Override
            public Div0 onBind(C condition) {
                return DivOperation1.this.apply0(base.bind(condition), expansion);
            }
        });
    }

    public <C1, C2> Div2<C1, C2> apply0(final Div2<C1, C2> base, final Div0 expansion) {
        return Div2.create(new Div2.OnBind<C1, C2>() {
            @Override
            public Div1<C2> onBind(C1 condition) {
                return DivOperation1.this.apply0(base.bind(condition), expansion);
            }
        });
    }

    public <C1, C2, C3> Div3<C1, C2, C3> apply0(final Div3<C1, C2, C3> base, final Div0 expansion) {
        return Div3.create(new Div3.OnBind<C1, C2, C3>() {
            @Override
            public Div2<C2, C3> onBind(C1 condition) {
                return DivOperation1.this.apply0(base.bind(condition), expansion);
            }
        });
    }

    public <C1, C2, C3, C4> Div4<C1, C2, C3, C4> apply0(final Div4<C1, C2, C3, C4> base, final Div0 expansion) {
        return Div4.create(new Div4.OnBind<C1, C2, C3, C4>() {
            @Override
            public Div3<C2, C3, C4> onBind(C1 condition) {
                return DivOperation1.this.apply0(base.bind(condition), expansion);
            }
        });
    }

    public <C1, C2, C3, C4, C5> Div5<C1, C2, C3, C4, C5> apply0(final Div5<C1, C2, C3, C4, C5> base, final Div0 expansion) {
        return Div5.create(new Div5.OnBind<C1, C2, C3, C4, C5>() {
            @Override
            public Div4<C2, C3, C4, C5> onBind(C1 condition) {
                return DivOperation1.this.apply0(base.bind(condition), expansion);
            }
        });
    }

    public <C> Div1<C> apply1(final Div0 base, final Div1<C> expansion) {
        return Div1.create(new Div1.OnBind<C>() {
            @NonNull
            @Override
            public Div0 onBind(final C condition) {
                return DivOperation1.this.apply0(base, expansion.bind(condition));
            }
        });
    }

    public <C1, C2> Div2<C1, C2> apply1(final Div1<C1> base, final Div1<C2> expansion) {
        return Div2.create((new Div2.OnBind<C1, C2>() {
            @Override
            public Div1<C2> onBind(C1 condition) {
                return DivOperation1.this.apply1(base.bind(condition), expansion);
            }
        }));
    }

    public <C1, C2, C3> Div3<C1, C2, C3> apply1(final Div2<C1, C2> base, final Div1<C3> expansion) {
        return Div3.create(new Div3.OnBind<C1, C2, C3>() {
            @Override
            public Div2<C2, C3> onBind(C1 condition) {
                return DivOperation1.this.apply1(base.bind(condition), expansion);
            }
        });
    }

    public <C1, C2> Div2<C1, C2> apply2(final Div0 base, final Div2<C1, C2> expansion) {
        return Div2.create(new Div2.OnBind<C1, C2>() {
            @Override
            public Div1<C2> onBind(C1 condition) {
                return DivOperation1.this.apply1(base, expansion.bind(condition));
            }
        });
    }

    public <C1, C2, C3> Div3<C1, C2, C3> apply2(final Div1<C1> base, final Div2<C2, C3> expansion) {
        return Div3.create(new Div3.OnBind<C1, C2, C3>() {
            @Override
            public Div2<C2, C3> onBind(C1 condition) {
                return DivOperation1.this.apply2(base.bind(condition), expansion);
            }
        });
    }

    public <C1, C2, C3, C4> Div4<C1, C2, C3, C4> apply2(final Div2<C1, C2> base, final Div2<C3, C4> expansion) {
        return Div4.create(new Div4.OnBind<C1, C2, C3, C4>() {
            @Override
            public Div3<C2, C3, C4> onBind(C1 condition) {
                return DivOperation1.this.apply2(base.bind(condition), expansion);
            }
        });
    }

    public <C1, C2, C3> Div3<C1, C2, C3> apply3(final Div0 base, final Div3<C1, C2, C3> expansion) {
        return Div3.create(new Div3.OnBind<C1, C2, C3>() {
            @Override
            public Div2<C2, C3> onBind(C1 condition) {
                return DivOperation1.this.apply2(base, expansion.bind(condition));
            }
        });
    }

    public <C1, C2, C3, C4> Div4<C1, C2, C3, C4> apply3(final Div1<C1> base, final Div3<C2, C3, C4> expansion) {
        return Div4.create(new Div4.OnBind<C1, C2, C3, C4>() {
            @Override
            public Div3<C2, C3, C4> onBind(C1 condition) {
                return DivOperation1.this.apply3(base.bind(condition), expansion);
            }
        });
    }

    public <C1, C2, C3, C4, C5> Div5<C1, C2, C3, C4, C5> apply3(final Div2<C1, C2> base, final Div3<C3, C4, C5> expansion) {
        return Div5.create(new Div5.OnBind<C1, C2, C3, C4, C5>() {
            @Override
            public Div4<C2, C3, C4, C5> onBind(C1 condition) {
                return DivOperation1.this.apply3(base.bind(condition), expansion);
            }
        });
    }

    public Div1<Div0> applyFuture0(final Div0 base) {
        return Div1.create(new Div1.OnBind<Div0>() {
            @NonNull
            @Override
            public Div0 onBind(Div0 expansion) {
                return DivOperation1.this.apply0(base, expansion);
            }
        });
    }

    public <C> Div2<C, Div0> applyFuture0(final Div1<C> base) {
        return Div2.create(new Div2.OnBind<C, Div0>() {
            @Override
            public Div1<Div0> onBind(C condition) {
                return DivOperation1.this.applyFuture0(base.bind(condition));
            }
        });
    }

    public <C1, C2> Div3<C1, C2, Div0> applyFuture0(final Div2<C1, C2> base) {
        return Div3.create(new Div3.OnBind<C1, C2, Div0>() {
            @Override
            public Div2<C2, Div0> onBind(C1 condition) {
                return DivOperation1.this.applyFuture0(base.bind(condition));
            }
        });
    }

    public <C1, C2, C3> Div4<C1, C2, C3, Div0> applyFuture0(final Div3<C1, C2, C3> base) {
        return Div4.create(new Div4.OnBind<C1, C2, C3, Div0>() {
            @Override
            public Div3<C2, C3, Div0> onBind(C1 condition) {
                return DivOperation1.this.applyFuture0(base.bind(condition));
            }
        });
    }

}
