package com.rubyhuntersky.gx.uis.divs.operations;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.basics.Sizelet;
import com.rubyhuntersky.gx.devices.poles.Pole;
import com.rubyhuntersky.gx.uis.divs.Div;
import com.rubyhuntersky.gx.uis.divs.Div0;

/**
 * @author wehjin
 * @since 2/9/16.
 */

public class ExpandVerticalDivOperation0 extends DivOperation0 {

    private final Sizelet heightlet;

    public ExpandVerticalDivOperation0(Sizelet heightlet) {
        this.heightlet = heightlet;
    }

    @Override
    public Div0 apply(final Div0 base) {
        return Div0.create(new Div.OnPresent() {
            @Override
            public void onPresent(@NonNull Div.Presenter presenter) {
                final Human human = presenter.getHuman();
                final Pole pole = presenter.getPole();
                final float expansion = heightlet.toFloat(human, pole.getRelatedHeight());
                final Pole shiftPole = pole.withShift(0, expansion);
                presenter.addPresentation(base.present(human, shiftPole, new Div.ForwardingObserver(presenter) {
                    @Override
                    public void onHeight(float height) {
                        super.onHeight(height + 2 * expansion);
                    }
                }));
            }
        });
    }
}
