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
public class PadHorizontalDivOperation0 extends DivOperation0 {

    private final Sizelet padlet;

    public PadHorizontalDivOperation0(Sizelet padlet) {
        this.padlet = padlet;
    }

    @Override
    public Div0 apply(final Div0 div) {
        return Div0.create(new Div.OnPresent() {
            @Override
            public void onPresent(@NonNull Div.Presenter presenter) {
                final Human human = presenter.getHuman();
                final Pole pole = presenter.getPole();
                final float padding = padlet.toFloat(human, pole.getFixedWidth());
                final float newWidth = pole.getFixedWidth() - 2 * padding;
                final Pole newPole = pole.withFixedWidth(newWidth).withShift(padding, 0);
                presenter.addPresentation(div.present(human, newPole, presenter));
            }
        });
    }
}
