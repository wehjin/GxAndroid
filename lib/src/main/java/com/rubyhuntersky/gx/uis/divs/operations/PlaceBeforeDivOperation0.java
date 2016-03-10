package com.rubyhuntersky.gx.uis.divs.operations;

import android.util.Pair;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.devices.poles.DelayPole;
import com.rubyhuntersky.gx.devices.poles.Pole;
import com.rubyhuntersky.gx.uis.OnPresent;
import com.rubyhuntersky.gx.internal.presenters.Presenter;
import com.rubyhuntersky.gx.presentations.Presentation;
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

    public PlaceBeforeDivOperation0(Div0 background, int gap) {
        this.background = background;
        this.gap = gap;
    }

    @Override
    public Div0 apply(final Div0 base) {
        return Div0.create(new OnPresent<Pole>() {
            @Override
            public void onPresent(Presenter<Pole> presenter) {
                Human human = presenter.getHuman();
                Pole pole = presenter.getDevice();
                final DelayPole delayColumn = pole.withElevation(pole.elevation + gap).withDelay();
                final Presentation frontPresentation = base.present(human, delayColumn, presenter);
                final Pole backgroundPole = pole.withRelatedHeight(frontPresentation.getHeight());
                final Presentation backgroundPresentation = background.present(human, backgroundPole, presenter);
                delayColumn.endDelay();
                final Pair<Presentation, Presentation> presentations =
                      new Pair<>(frontPresentation, backgroundPresentation);
                presenter.addPresentation(presentations.first);
                presenter.addPresentation(presentations.second);
            }
        });
    }
}
