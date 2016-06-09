package com.rubyhuntersky.gx.uis.divs.operations;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.devices.poles.Pole;
import com.rubyhuntersky.gx.devices.poles.ShiftPole;
import com.rubyhuntersky.gx.internal.presenters.Presenter;
import com.rubyhuntersky.gx.presentations.Presentation;
import com.rubyhuntersky.gx.uis.OnPresent;
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
        return Div0.create(new OnPresent<Pole>() {
            @Override
            public void onPresent(Presenter<Pole> presenter) {
                Human human = presenter.getHuman();
                Pole pole = presenter.getDevice();
                final ShiftPole nearDelayPole = pole.withElevation(pole.getElevation() + gap).withShift();
                final Presentation nearPresentation = base.present(human, nearDelayPole, presenter);
                final float nearHeight = nearPresentation.getHeight();

                final ShiftPole farDelayPole = pole.withRelatedHeight(nearHeight).withShift();
                final Presentation farPresentation = background.present(human, farDelayPole, presenter);
                final float farHeight = farPresentation.getHeight();

                if (farHeight > nearHeight) {
                    nearDelayPole.doShift(0, (farHeight - nearHeight) * anchor);
                    farDelayPole.doShift(0, 0);
                } else if (nearHeight > farHeight) {
                    nearDelayPole.doShift(0, 0);
                    farDelayPole.doShift(0, (nearHeight - farHeight) * anchor);
                } else {
                    nearDelayPole.doShift(0, 0);
                    farDelayPole.doShift(0, 0);
                }

                final Pair<Presentation, Presentation> presentations = new Pair<>(nearPresentation, farPresentation);
                presenter.addPresentation(presentations.first);
                presenter.addPresentation(presentations.second);
            }
        });
    }
}
