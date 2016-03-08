package com.rubyhuntersky.gx.columns;

import com.rubyhuntersky.gx.Observer;
import com.rubyhuntersky.gx.Reaction;
import com.rubyhuntersky.gx.basics.Sizelet;
import com.rubyhuntersky.gx.columns.operations.ExpandVerticalDivOperation0;
import com.rubyhuntersky.gx.columns.operations.PadHorizontalDivOperation0;
import com.rubyhuntersky.gx.columns.operations.PlaceBeforeDivOperation0;
import com.rubyhuntersky.gx.presenters.OnPresent;
import com.rubyhuntersky.gx.presenters.Presenter;
import com.rubyhuntersky.gx.presenters.SwitchPresenter;

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
        return Div0.create(new OnPresent<Column>() {

            @Override
            public void onPresent(final Presenter<Column> presenter) {
                final SwitchPresenter<Column> switchPresenter = new SwitchPresenter<>(presenter.getHuman(),
                                                                                      presenter.getDisplay(),
                                                                                      presenter);
                presenter.addPresentation(switchPresenter);
                present(repl, switchPresenter);
            }

            void present(final Repl<C1, C2, C3, C4> repl, final SwitchPresenter<Column> presenter) {
                if (presenter.isCancelled())
                    return;

                final Div0 ui = repl.print(Div4.this);
                final Observer observer = new Observer() {
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
                };
                presenter.addPresentation(ui.present(presenter.getHuman(), presenter.getDisplay(), observer));
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
