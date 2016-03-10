package com.rubyhuntersky.gx.uis.divs;

import com.rubyhuntersky.gx.observers.Observer;
import com.rubyhuntersky.gx.reactions.Reaction;
import com.rubyhuntersky.gx.basics.Sizelet;
import com.rubyhuntersky.gx.devices.poles.Pole;
import com.rubyhuntersky.gx.uis.divs.operations.ExpandDownDivOperation1;
import com.rubyhuntersky.gx.uis.divs.operations.ExpandVerticalDivOperation0;
import com.rubyhuntersky.gx.uis.divs.operations.PadHorizontalDivOperation0;
import com.rubyhuntersky.gx.uis.divs.operations.PlaceBeforeDivOperation0;
import com.rubyhuntersky.gx.uis.OnPresent;
import com.rubyhuntersky.gx.internal.presenters.Presenter;
import com.rubyhuntersky.gx.internal.presenters.SwitchPresenter;

/**
 * @author wehjin
 * @since 1/30/16.
 */

public abstract class Div2<C1, C2> {

    public abstract Div1<C2> bind(C1 condition);

    public static <C1, C2> Div2<C1, C2> create(final OnBind<C1, C2> onBind) {
        return new Div2<C1, C2>() {
            @Override
            public Div1<C2> bind(C1 condition) {
                return onBind.onBind(condition);
            }
        };
    }

    public Div2<C1, C2> padHorizontal(final Sizelet padlet) {
        return new PadHorizontalDivOperation0(padlet).apply(this);
    }

    public Div2<C1, C2> placeBefore(final Div0 background, final int gap) {
        return new PlaceBeforeDivOperation0(background, gap).apply(this);
    }

    public Div3<C1, C2, Div0> expandDown() {
        return new ExpandDownDivOperation1().applyFuture0(this);
    }

    public Div2<C1, C2> expandDown(final Div0 expansion) {
        return new ExpandDownDivOperation1().apply0(this, expansion);
    }

    public <C3> Div3<C1, C2, C3> expandDown(final Div1<C3> expansion) {
        return new ExpandDownDivOperation1().apply1(this, expansion);
    }

    public <C3, C4> Div4<C1, C2, C3, C4> expandDown(final Div2<C3, C4> expansion) {
        return new ExpandDownDivOperation1().apply2(this, expansion);
    }

    public <C3, C4, C5> Div5<C1, C2, C3, C4, C5> expandDown(final Div3<C3, C4, C5> expansion) {
        return new ExpandDownDivOperation1().apply3(this, expansion);
    }

    public Div2<C1, C2> expandVertical(final Sizelet heightlet) {
        return new ExpandVerticalDivOperation0(heightlet).apply(this);
    }

    public Div0 printReadEval(final Repl<C1, C2> repl) {
        final Div2<C1, C2> div2 = this;
        return Div0.create(new OnPresent<Pole>() {

            @Override
            public void onPresent(final Presenter<Pole> presenter) {
                final SwitchPresenter<Pole> switchPresenter = new SwitchPresenter<>(presenter.getHuman(),
                                                                                      presenter.getDevice(),
                                                                                      presenter);
                presenter.addPresentation(switchPresenter);
                present(repl, switchPresenter);
            }

            void present(final Repl<C1, C2> repl, final SwitchPresenter<Pole> presenter) {
                if (presenter.isCancelled())
                    return;
                final Div0 print = repl.print(div2);
                presenter.addPresentation(print.present(presenter.getHuman(), presenter.getDevice(), new Observer() {
                    @Override
                    public void onReaction(Reaction reaction) {
                        if (presenter.isCancelled())
                            return;
                        repl.read(reaction);
                        if (repl.eval()) {
                            present(repl, presenter);
                        }
                    }

                    @Override
                    public void onEnd() {
                        presenter.onEnd();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        presenter.onError(throwable);
                    }
                }));
            }
        });
    }

    public interface Repl<C1, C2> {
        Div0 print(Div2<C1, C2> unbound);
        void read(Reaction reaction);
        boolean eval();
    }


    public interface OnBind<C1, C2> {
        Div1<C2> onBind(C1 condition);
    }

}
