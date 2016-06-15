package com.rubyhuntersky.gx.uis.divs;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.Gx;
import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.basics.Frame;
import com.rubyhuntersky.gx.basics.Removable;
import com.rubyhuntersky.gx.basics.Sizelet;
import com.rubyhuntersky.gx.basics.Spot;
import com.rubyhuntersky.gx.devices.poles.Pole;
import com.rubyhuntersky.gx.devices.poles.ShiftPole;
import com.rubyhuntersky.gx.internal.surface.Jester;
import com.rubyhuntersky.gx.reactions.Reaction;
import com.rubyhuntersky.gx.uis.divs.operations.ExpandDownDivOperation1;
import com.rubyhuntersky.gx.uis.divs.operations.ExpandVerticalDivOperation0;
import com.rubyhuntersky.gx.uis.divs.operations.PadHorizontalDivOperation0;
import com.rubyhuntersky.gx.uis.divs.operations.PlaceBeforeDivOperation0;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public abstract class Div0 implements Div {

    static final String TAG = Div0.class.getSimpleName();
    public static final Div0 EMPTY = new Div0() {
        @NonNull
        @Override
        public Presentation present(@NonNull Human human, @NonNull Pole pole, @NonNull Observer observer) {
            observer.onHeight(0);
            return Presentation.EMPTY.INSTANCE;
        }
    };

    @NonNull
    public abstract Div.Presentation present(@NonNull Human human, @NonNull Pole pole, @NonNull Div.Observer observer);

    private Div0() {
    }

    public static Div0 create(final Div.OnPresent onPresent) {
        return new Div0() {
            @NonNull
            @Override
            public Div.Presentation present(@NonNull Human human, @NonNull final Pole pole, @NonNull Div.Observer observer) {
                final Div.BasePresenter presenter = new Div.BasePresenter(human, pole, observer);
                onPresent.onPresent(presenter);
                return presenter;
            }
        };
    }

    public <T> Div0 enableTap(final T label) {
        final Div0 upstream = this;
        return create(new Div.OnPresent() {
            @Override
            public void onPresent(@NonNull final Div.Presenter presenter) {
                presenter.addPresentation(new Div.PresenterPresentation(presenter) {

                    private Presentation presentation;
                    private Removable surface;

                    {
                        presentation = upstream.present(getHuman(), getPole(), new ForwardingObserver(presenter) {
                            @Override
                            public void onHeight(float height) {
                                updateSurface(height);
                                super.onHeight(height);
                            }
                        });

                    }

                    private void updateSurface(float height) {
                        if (surface != null) {
                            surface.remove();
                        }
                        final Pole pole = getPole();
                        surface = pole.addSurface(new Frame(pole.getWidth(), height, pole.getElevation()),
                                                  new Jester() {
                                                      @Nullable
                                                      @Override
                                                      public Contact getContact(@NotNull final Spot downSpot) {
                                                          return new TapContact<>(downSpot,
                                                                                  presenter, presenter.getHuman(),
                                                                                  label);
                                                      }
                                                  });
                    }

                    @Override
                    public void onCancel() {
                        if (surface != null) {
                            surface.remove();
                        }
                        presentation.cancel();
                    }
                });
            }
        });
    }

    public Div0 padHorizontal(final Sizelet padlet) {
        return new PadHorizontalDivOperation0(padlet).apply(this);
    }

    public Div0 padTop(final Sizelet padlet) {
        final Div0 upstream = this;
        return create(new Div.OnPresent() {
            @Override
            public void onPresent(@NonNull final Div.Presenter presenter) {
                final Human human = presenter.getHuman();
                final ShiftPole shiftPole = presenter.getPole().withShift();
                presenter.addPresentation(upstream.present(human, shiftPole, new ForwardingObserver(presenter) {
                    @Override
                    public void onHeight(float height) {
                        final float padding = padlet.toFloat(human, height);
                        shiftPole.doShift(0, padding);
                        super.onHeight(height + padding);
                    }
                }));
            }
        });
    }

    public Div0 padBottom(final Sizelet padlet) {
        return expandDown(Gx.INSTANCE.gapColumn(padlet));
    }

    public Div0 padVertical(final Sizelet padlet) {
        final Div0 upstream = this;
        return create(new Div.OnPresent() {

            @Override
            public void onPresent(@NotNull final Presenter presenter) {
                final Human human = presenter.getHuman();
                final ShiftPole shiftPole = presenter.getPole().withShift();
                presenter.addPresentation(upstream.present(human, shiftPole, new ForwardingObserver(presenter) {
                    @Override
                    public void onHeight(float height) {
                        final float padding = padlet.toFloat(human, height);
                        shiftPole.doShift(0, padding);
                        final float fullHeight = height + 2 * padding;
                        super.onHeight(fullHeight);
                    }
                }));
            }
        });
    }

    public Div0 expandVertical(final Sizelet heightlet) {
        return new ExpandVerticalDivOperation0(heightlet).apply(this);
    }

    public Div0 placeBefore(@NonNull final Div0 background, final int gap) {
        return new PlaceBeforeDivOperation0(background, gap).apply(this);
    }

    public Div0 placeBefore(@NonNull final Div0 background, final int gap, final float anchor) {
        return new PlaceBeforeDivOperation0(background, gap, anchor).apply(this);
    }

    public Div1<Div0> expandDown() {
        return new ExpandDownDivOperation1().applyFuture0(this);
    }

    public Div0 expandDown(@NonNull final Div0 expansion) {
        return new ExpandDownDivOperation1().apply0(this, expansion);
    }

    public <C> Div1<C> expandDown(final Div1<C> expansion) {
        return new ExpandDownDivOperation1().apply1(this, expansion);
    }

    public <C1, C2> Div2<C1, C2> expandDown(final Div2<C1, C2> expansion) {
        return new ExpandDownDivOperation1().apply2(this, expansion);
    }

    public <C1, C2, C3> Div3<C1, C2, C3> expandDown(final Div3<C1, C2, C3> expansion) {
        return new ExpandDownDivOperation1().apply3(this, expansion);
    }

    public Div0 isolate() {
        final Div0 upstream = Div0.this;
        return create(new Div.OnPresent() {
            @Override
            public void onPresent(@NonNull final Div.Presenter presenter) {
                final Human human = presenter.getHuman();
                final Pole pole = presenter.getPole();
                presenter.addPresentation(upstream.present(human, pole, new Div.ForwardingObserver(presenter) {
                    @Override
                    public void onReaction(@NonNull Reaction reaction) {
                        // Trap reaction. Do nothing.
                    }
                }));
            }
        });
    }

}
