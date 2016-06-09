package com.rubyhuntersky.gx.uis.divs;

import android.support.annotation.NonNull;
import android.util.Log;

import com.rubyhuntersky.gx.Gx;
import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.basics.Frame;
import com.rubyhuntersky.gx.basics.Removable;
import com.rubyhuntersky.gx.basics.Sizelet;
import com.rubyhuntersky.gx.basics.Spot;
import com.rubyhuntersky.gx.devices.poles.Pole;
import com.rubyhuntersky.gx.devices.poles.ShiftPole;
import com.rubyhuntersky.gx.internal.presenters.BasePresenter;
import com.rubyhuntersky.gx.internal.presenters.Presenter;
import com.rubyhuntersky.gx.internal.surface.Jester;
import com.rubyhuntersky.gx.internal.surface.MoveReaction;
import com.rubyhuntersky.gx.internal.surface.UpReaction;
import com.rubyhuntersky.gx.observers.Observer;
import com.rubyhuntersky.gx.presentations.Presentation;
import com.rubyhuntersky.gx.presentations.ResizePresentation;
import com.rubyhuntersky.gx.reactions.Reaction;
import com.rubyhuntersky.gx.reactions.TapReaction;
import com.rubyhuntersky.gx.uis.OnPresent;
import com.rubyhuntersky.gx.uis.core.Ui0;
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

public abstract class Div0 implements Ui0<Pole> {

    static final String TAG = Div0.class.getSimpleName();

    static final public Div0 EMPTY = new Div0() {
        @Override
        public Presentation present(Human human, Pole pole, Observer observer) {
            return Presentation.EMPTY;
        }
    };

    private Div0() {
    }

    public static Div0 create(final OnPresent<Pole> onPresent) {
        return new Div0() {
            @Override
            public Presentation present(Human human, final Pole pole, Observer observer) {
                final BasePresenter<Pole> presenter = new BasePresenter<Pole>(human, pole, observer) {
                    @Override
                    public float getWidth() {
                        return device.getFixedWidth();
                    }

                    @Override
                    public float getHeight() {
                        float union = 0;
                        for (Presentation presentation : presentations) {
                            union = Math.max(union, presentation.getHeight());
                        }
                        return union;
                    }
                };
                onPresent.onPresent(presenter);
                return presenter;
            }
        };
    }

    public abstract Presentation present(Human human, Pole pole, Observer observer);

    public <T> Div0 enableTap(final T label) {
        final Div0 upstream = this;
        return create(new OnPresent<Pole>() {
            @Override
            public void onPresent(final Presenter<Pole> presenter) {
                final Human human = presenter.getHuman();
                final Pole pole = presenter.getDevice();
                Presentation presentation = upstream.present(human, pole, presenter);
                presenter.addPresentation(presentation);

                final Frame frame = new Frame(presentation.getWidth(), presentation.getHeight(), pole.getElevation());
                final Removable removable = pole.addSurface(frame, new Jester() {
                    @Nullable
                    @Override
                    public Contact getContact(@NotNull final Spot downSpot) {

                        return new Contact() {
                            @Override
                            public void doCancel() {
                                Log.d(TAG, "doCancel");
                            }

                            @NotNull
                            @Override
                            public MoveReaction getMoveReaction(@NotNull Spot spot) {
                                if (isOutOfBounds(spot)) {
                                    return MoveReaction.CANCEL;
                                } else {
                                    return MoveReaction.CONTINUE;
                                }
                            }

                            private boolean isOutOfBounds(@NotNull Spot spot) {
                                return spot.distanceSquared(downSpot) > 100;
                            }

                            @NotNull
                            @Override
                            public Contact doMove(@NotNull Spot spot) {
                                return this;
                            }

                            @NotNull
                            @Override
                            public UpReaction getUpReaction(@NotNull Spot spot) {
                                if (isOutOfBounds(spot)) {
                                    return UpReaction.CANCEL;
                                } else {
                                    return UpReaction.CONFIRM;
                                }
                            }

                            @Override
                            public void doUp(@NotNull Spot spot) {
                                long time = System.currentTimeMillis();
                                Log.d(TAG, "doUp " + time);
                                presenter.onReaction(new TapReaction<>(label, "enableTap", time));
                            }
                        };
                    }
                });
                presenter.addPresentation(new Presentation() {

                    boolean cancelled = false;

                    @Override
                    public float getWidth() {
                        return frame.getHorizontal().toLength();
                    }

                    @Override
                    public float getHeight() {
                        return frame.getVertical().toLength();
                    }

                    @Override
                    public boolean isCancelled() {
                        return cancelled;
                    }

                    @Override
                    public void cancel() {
                        if (!cancelled) {
                            cancelled = true;
                            removable.remove();
                        }
                    }
                });
            }
        });
    }

    public Div0 padHorizontal(final Sizelet padlet) {
        return new PadHorizontalDivOperation0(padlet).apply(this);
    }

    public Div0 padTop(final Sizelet padlet) {
        final Div0 ui = this;
        return create(new OnPresent<Pole>() {
            @Override
            public void onPresent(Presenter<Pole> presenter) {
                final Human human = presenter.getHuman();
                final Pole pole = presenter.getDevice();
                final ShiftPole newColumn = pole.withShift();
                final Presentation presentation = ui.present(human, newColumn, presenter);
                final float height = presentation.getHeight();
                final float padding = padlet.toFloat(human, height);
                newColumn.doShift(0, padding);
                final float newHeight = height + padding;
                presenter.addPresentation(new ResizePresentation(pole.getFixedWidth(), newHeight, presentation));
            }
        });
    }

    public Div0 padBottom(final Sizelet padlet) {
        return expandDown(Gx.INSTANCE.gapColumn(padlet));
    }

    public Div0 padVertical(final Sizelet padlet) {
        final Div0 ui = this;
        return create(new OnPresent<Pole>() {
            @Override
            public void onPresent(Presenter<Pole> presenter) {
                final Human human = presenter.getHuman();
                final Pole pole = presenter.getDevice();
                final ShiftPole newColumn = pole.withShift();
                final Presentation presentation = ui.present(human, newColumn, presenter);
                final float height = presentation.getHeight();
                final float padding = padlet.toFloat(human, height);
                newColumn.doShift(0, padding);
                final float newHeight = height + 2 * padding;
                presenter.addPresentation(new ResizePresentation(pole.getFixedWidth(), newHeight, presentation));
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
        return create(new OnPresent<Pole>() {
            @Override
            public void onPresent(final Presenter<Pole> presenter) {
                final Presentation presentation = Div0.this.present(presenter.getHuman(),
                                                                    presenter.getDevice(),
                                                                    new Observer() {
                                                                        @Override
                                                                        public void onReaction(@NonNull Reaction reaction) {
                                                                            // Do nothing.
                                                                        }

                                                                        @Override
                                                                        public void onEnd() {
                                                                            // Do nothing.
                                                                        }

                                                                        @Override
                                                                        public void onError(@NonNull Throwable throwable) {
                                                                            presenter.onError(throwable);
                                                                        }
                                                                    });
                presenter.addPresentation(presentation);
            }
        });
    }
}
