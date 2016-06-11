package com.rubyhuntersky.gx.uis.divs.operations;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.devices.poles.Pole;
import com.rubyhuntersky.gx.devices.poles.ShiftPole;
import com.rubyhuntersky.gx.uis.divs.Div;
import com.rubyhuntersky.gx.uis.divs.Div0;

/**
 * @author wehjin
 * @since 2/9/16.
 */

public class ExpandDownDivOperation1 extends DivOperation1 {

    @Override
    public Div0 apply0(final Div0 base, final Div0 more) {
        return Div0.create(new Div.OnPresent() {
            @Override
            public void onPresent(@NonNull final Div.Presenter presenter) {
                presenter.addPresentation(new Div.PresenterPresentation(presenter) {

                    private Div.Presentation top;
                    private Div.Presentation bottom;
                    private ShiftPole bottomPole;
                    private Float bottomHeight;

                    {
                        final Human human = getHuman();
                        final Pole pole = getPole();
                        top = base.present(human, pole, new Div.ForwardingObserver(presenter) {
                            @Override
                            public void onHeight(float height) {
                                presentExpansion(height);
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                        top.cancel();
                        if (bottom != null) {
                            bottom.cancel();
                        }
                    }

                    private void presentExpansion(final float topHeight) {
                        if (bottom != null) {
                            bottomPole.doShift(0, topHeight);
                            if (bottomHeight != null) {
                                presenter.onHeight(topHeight + bottomHeight);
                            }
                            return;
                        }
                        bottomPole = getPole().withRelatedHeight(topHeight).withShift(0, topHeight);
                        bottom = more.present(getHuman(), bottomPole, new Div.ForwardingObserver(presenter) {
                            @Override
                            public void onHeight(float height) {
                                bottomHeight = height;
                                super.onHeight(topHeight + height);
                            }
                        });
                    }
                });
            }
        });
    }
}
