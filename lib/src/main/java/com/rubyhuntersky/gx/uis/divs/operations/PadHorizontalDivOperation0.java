package com.rubyhuntersky.gx.uis.divs.operations;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.basics.Sizelet;
import com.rubyhuntersky.gx.devices.poles.Pole;
import com.rubyhuntersky.gx.uis.OnPresent;
import com.rubyhuntersky.gx.internal.presenters.Presenter;
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
    public Div0 apply(final Div0 base) {
        return Div0.create(new OnPresent<Pole>() {
            @Override
            public void onPresent(Presenter<Pole> presenter) {
                Human human = presenter.getHuman();
                Pole pole = presenter.getDevice();
                final float padding = padlet.toFloat(human, pole.fixedWidth);
                Pole newPole = pole.withFixedWidth(pole.fixedWidth - 2 * padding).withShift(padding, 0);
                presenter.addPresentation(base.present(human, newPole, presenter));
            }
        });
    }
}
