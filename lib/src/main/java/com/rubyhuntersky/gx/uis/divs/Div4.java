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

public abstract class Div4<C1, C2, C3, C4> {

    public static <C1, C2, C3, C4> Div4<C1, C2, C3, C4> create(final OnBind<C1, C2, C3, C4> onBind) {
        return new Div4<C1, C2, C3, C4>() {
            @Override
            public Div3<C2, C3, C4> bind(C1 condition) {
                return onBind.onBind(condition);
            }
        };
    }

    public abstract Div3<C2, C3, C4> bind(C1 condition);

    public Div4<C1, C2, C3, C4> expandVertical(final Sizelet heightlet) {
        return new ExpandVerticalDivOperation0(heightlet).apply(this);
    }

    public Div4<C1, C2, C3, C4> padHorizontal(final Sizelet padlet) {
        return new PadHorizontalDivOperation0(padlet).apply(this);
    }

    public Div4<C1, C2, C3, C4> placeBefore(Div0 behind, int gap) {
        return new PlaceBeforeDivOperation0(behind, gap).apply(this);
    }

    public Div0 printReadEval(final Repl<C1, C2, C3, C4> repl) {
        return Div0.create(new OnPresent<Pole>() {

            @Override
            public void onPresent(final Presenter<Pole> presenter) {
                final SwitchPresenter<Pole> switchPresenter = new SwitchPresenter<>(presenter.getHuman(),
                                                                                      presenter.getDevice(),
                                                                                      presenter);
                presenter.addPresentation(switchPresenter);
                present(repl, switchPresenter);
            }

            void present(final Repl<C1, C2, C3, C4> repl, final SwitchPresenter<Pole> presenter) {
                if (presenter.isCancelled())
                    return;

                final Div0 ui = repl.print(Div4.this);
                final Observer observer = new Observer() {
                    @Override
                    public void onReaction(@NonNull Reaction reaction) {
                        if (presenter.isCancelled())
                            return;

                        repl.read(reaction);
                        if (repl.eval()) {
                            present(repl, presenter);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        presenter.onError(throwable);
                    }
                };
                presenter.addPresentation(ui.present(presenter.getHuman(), presenter.getDevice(), observer));
            }
        });
    }


    public interface Repl<C1, C2, C3, C4> {
        Div0 print(Div4<C1, C2, C3, C4> unbound);
        void read(Reaction reaction);
        boolean eval();
    }


    public interface OnBind<C1, C2, C3, C4> {
        Div3<C2, C3, C4> onBind(C1 condition);
    }

}
