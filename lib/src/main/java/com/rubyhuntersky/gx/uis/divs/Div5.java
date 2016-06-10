package com.rubyhuntersky.gx.uis.divs;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.observers.Observer;
import com.rubyhuntersky.gx.reactions.Reaction;
import com.rubyhuntersky.gx.basics.Sizelet;
import com.rubyhuntersky.gx.devices.poles.Pole;
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
        return Div0.create(new OnPresent<Pole>() {

            @Override
            public void onPresent(final Presenter<Pole> presenter) {
                final SwitchPresenter<Pole> switchPresenter = new SwitchPresenter<>(presenter.getHuman(),
                                                                                      presenter.getDevice(),
                                                                                      presenter);
                presenter.addPresentation(switchPresenter);
                present(repl, switchPresenter);
            }

            void present(final Repl<C1, C2, C3, C4, C5> repl, final SwitchPresenter<Pole> switchPresenter) {
                if (switchPresenter.isCancelled())
                    return;

                final Div0 ui = repl.print(Div5.this);
                final Observer observer = new Observer() {
                    @Override
                    public void onReaction(@NonNull Reaction reaction) {
                        if (switchPresenter.isCancelled())
                            return;

                        repl.read(reaction);
                        if (repl.eval()) {
                            present(repl, switchPresenter);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        switchPresenter.onError(throwable);
                    }
                };
                switchPresenter.addPresentation(ui.present(switchPresenter.getHuman(),
                                                           switchPresenter.getDevice(),
                                                           observer));
            }
        });
    }


    public interface Repl<C1, C2, C3, C4, C5> {
        Div0 print(Div5<C1, C2, C3, C4, C5> unbound);
        void read(Reaction reaction);
        boolean eval();
    }


    public interface OnBind<C1, C2, C3, C4, C5> {
        Div4<C2, C3, C4, C5> onBind(C1 condition);
    }

}
