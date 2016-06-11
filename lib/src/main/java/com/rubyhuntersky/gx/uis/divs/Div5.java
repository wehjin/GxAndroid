package com.rubyhuntersky.gx.uis.divs;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.Gx;
import com.rubyhuntersky.gx.basics.Sizelet;
import com.rubyhuntersky.gx.reactions.Reaction;
import com.rubyhuntersky.gx.uis.divs.operations.ExpandVerticalDivOperation0;
import com.rubyhuntersky.gx.uis.divs.operations.PadHorizontalDivOperation0;
import com.rubyhuntersky.gx.uis.divs.operations.PlaceBeforeDivOperation0;

/**
 * @author wehjin
 * @since 1/30/16.
 */

public abstract class Div5<C1, C2, C3, C4, C5> {

    public static <C1, C2, C3, C4, C5> Div5<C1, C2, C3, C4, C5> create(final OnBind<C1, C2, C3, C4, C5> onBind) {
        return new Div5<C1, C2, C3, C4, C5>() {
            @Override
            public Div4<C2, C3, C4, C5> bind(C1 condition) {
                return onBind.onBind(condition);
            }
        };
    }

    public abstract Div4<C2, C3, C4, C5> bind(C1 condition);

    public Div5<C1, C2, C3, C4, C5> expandVertical(final Sizelet heightlet) {
        return new ExpandVerticalDivOperation0(heightlet).apply(this);
    }

    public Div5<C1, C2, C3, C4, C5> padHorizontal(final Sizelet padlet) {
        return new PadHorizontalDivOperation0(padlet).apply(this);
    }

    public Div5<C1, C2, C3, C4, C5> placeBefore(Div0 behind, int gap) {
        return new PlaceBeforeDivOperation0(behind, gap).apply(this);
    }

    public Div0 printReadEval(final Repl<C1, C2, C3, C4, C5> repl) {
        return Gx.INSTANCE.printReadEvaluate(repl, this);
    }


    public interface Repl<C1, C2, C3, C4, C5> extends Div.PrintReadEvaluater<Div5> {
        Div0 print(Div5<C1, C2, C3, C4, C5> unbound);
        void read(@NonNull Reaction reaction);
        boolean eval();
    }


    public interface OnBind<C1, C2, C3, C4, C5> {
        Div4<C2, C3, C4, C5> onBind(C1 condition);
    }

}
