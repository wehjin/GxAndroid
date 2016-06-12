package com.rubyhuntersky.gx.uis.divs.operations;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.devices.poles.Pole;
import com.rubyhuntersky.gx.devices.poles.ShiftPole;
import com.rubyhuntersky.gx.uis.divs.Div;
import com.rubyhuntersky.gx.uis.divs.Div0;

/**
 * TODO Extend DivOperation1.
 *
 * @author wehjin
 * @since 2/9/16.
 */

public class PlaceBeforeDivOperation0 extends DivOperation0 {
    private final Div0 background;
    private final int gap;
    private final float anchor;

    public PlaceBeforeDivOperation0(Div0 background, int gap) {
        this.background = background;
        this.gap = gap;
        this.anchor = 0f;
    }

    public PlaceBeforeDivOperation0(@NonNull Div0 background, int gap, float anchor) {
        this.background = background;
        this.gap = gap;
        this.anchor = anchor;
    }

    @Override
    public Div0 apply(final Div0 base) {
        return Div0.create(new Div.OnPresent() {
            @Override
            public void onPresent(@NonNull final Div.Presenter presenter) {
                presenter.addPresentation(new Div.PresenterPresentation(presenter) {

                    private ShiftPole nearPole;
                    private Div.Presentation farPresentation = Div.Presentation.EMPTY.INSTANCE;
                    private Div.Presentation nearPresentation = Div.Presentation.EMPTY.INSTANCE;

                    {
                        final Human human = getHuman();
                        final Pole pole = getPole();
                        nearPole = pole.withElevation(pole.getElevation() + gap).withShift();
                        nearPresentation = base.present(human, nearPole, new Div.ForwardingObserver(presenter) {
                            @Override
                            public void onHeight(float nearHeight) {
                                presentFar(nearHeight);
                            }
                        });

                    }

                    private void presentFar(final float nearHeight) {
                        final Human human = getHuman();
                        final Pole pole = getPole();
                        final ShiftPole farPole = pole.withRelatedHeight(nearHeight).withShift();
                        farPresentation.cancel();
                        farPresentation = background.present(human, farPole, new Div.ForwardingObserver(presenter) {
                            @Override
                            public void onHeight(float farHeight) {
                                float maxHeight;
                                if (farHeight > nearHeight) {
                                    maxHeight = farHeight;
                                    nearPole.doShift(0, (farHeight - nearHeight) * anchor);
                                    farPole.doShift(0, 0);
                                } else if (nearHeight > farHeight) {
                                    maxHeight = nearHeight;
                                    nearPole.doShift(0, 0);
                                    farPole.doShift(0, (nearHeight - farHeight) * anchor);
                                } else {
                                    maxHeight = nearHeight;
                                    nearPole.doShift(0, 0);
                                    farPole.doShift(0, 0);
                                }
                                super.onHeight(maxHeight);
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                        farPresentation.cancel();
                        nearPresentation.cancel();
                    }
                });
            }
        });
    }
}
