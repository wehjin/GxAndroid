package com.rubyhuntersky.gx.uis.divs.operations;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.basics.Sizelet;
import com.rubyhuntersky.gx.devices.poles.Pole;
import com.rubyhuntersky.gx.uis.OnPresent;
import com.rubyhuntersky.gx.internal.presenters.Presenter;
import com.rubyhuntersky.gx.presentations.Presentation;
import com.rubyhuntersky.gx.presentations.ResizePresentation;
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
        return Div0.create(new OnPresent<Pole>() {
            @Override
            public void onPresent(Presenter<Pole> presenter) {
                final Human human = presenter.getHuman();
                final Pole pole = presenter.getDevice();
                final float expansion = heightlet.toFloat(human, pole.relatedHeight);
                final Pole shiftPole = pole.withShift(0, expansion);
                final Presentation present = base.present(human, shiftPole, presenter);
                final float expanded = present.getHeight() + 2 * expansion;
                final Presentation resize = new ResizePresentation(pole.fixedWidth, expanded, present);
                presenter.addPresentation(resize);
            }
        });
    }
}
