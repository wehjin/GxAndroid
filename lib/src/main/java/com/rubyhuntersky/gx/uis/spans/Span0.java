package com.rubyhuntersky.gx.uis.spans;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.basics.Sizelet;
import com.rubyhuntersky.gx.devices.bars.Bar;
import com.rubyhuntersky.gx.devices.bars.ShiftBar;
import com.rubyhuntersky.gx.devices.poles.Pole;
import com.rubyhuntersky.gx.internal.presenters.BasePresenter;
import com.rubyhuntersky.gx.internal.presenters.Presenter;
import com.rubyhuntersky.gx.observers.Observer;
import com.rubyhuntersky.gx.presentations.Presentation;
import com.rubyhuntersky.gx.presentations.ResizePresentation;
import com.rubyhuntersky.gx.uis.OnPresent;
import com.rubyhuntersky.gx.uis.core.Ui0;
import com.rubyhuntersky.gx.uis.divs.Div0;
import com.rubyhuntersky.gx.uis.tiles.Tile0;

/**
 * @author wehjin
 * @since 1/27/16.
 */

abstract public class Span0 implements Ui0<Bar> {

    @Override
    abstract public Presentation present(Human human, Bar bar, Observer observer);

    public Div0 toColumn(final Sizelet heightlet) {
        final Span0 span0 = this;
        return Div0.create(new OnPresent<Pole>() {
            @Override
            public void onPresent(Presenter<Pole> presenter) {
                presenter.addPresentation(
                      presentBarToColumn(span0, heightlet, presenter.getHuman(), presenter.getDevice(), presenter));
            }
        });
    }

    @NonNull
    private Presentation presentBarToColumn(Span0 span0, Sizelet heightlet, Human human, Pole pole,
          Observer observer) {
        final float height = heightlet.toFloat(human, pole.getRelatedHeight());
        final Bar bar = new Bar(height, pole.getFixedWidth(), pole.getElevation(), pole);
        final ShiftBar shiftBar = bar.withShift();
        final Presentation presentation = span0.present(human, shiftBar, observer);
        final float presentationWidth = presentation.getWidth();
        final float extraWidth = pole.getFixedWidth() - presentationWidth;
        final float anchor = .5f;
        shiftBar.doShift(extraWidth * anchor, 0);
        return new ResizePresentation(pole.getFixedWidth(), bar.fixedHeight, presentation);
    }

    public Span0 expandStart(final Tile0 startUi) {
        return expandStart(startUi.toBar());
    }

    public Span0 expandStart(final Span0 startUi) {
        final Span0 ui = this;
        return create(new OnPresent<Bar>() {
            @Override
            public void onPresent(Presenter<Bar> presenter) {
                final Bar bar = presenter.getDevice();
                final ShiftBar shiftBar = bar.withShift();
                final Human human = presenter.getHuman();
                final Presentation endPresentation = ui.present(human, shiftBar, presenter);
                final Bar startBar = bar.withRelatedWidth(endPresentation.getWidth());
                final Presentation startPresentation = startUi.present(human, startBar, presenter);
                final float startWidth = startPresentation.getWidth();
                shiftBar.doShift(startWidth, 0);
                final float combinedWidth = startWidth + endPresentation.getWidth();
                presenter.addPresentation(startPresentation);
                presenter.addPresentation(new ResizePresentation(combinedWidth, bar.fixedHeight, endPresentation));
            }
        });
    }

    public Span0 padStart(final Sizelet padlet) {
        final Span0 ui = this;
        return create(new OnPresent<Bar>() {
            @Override
            public void onPresent(Presenter<Bar> presenter) {
                final Bar bar = presenter.getDevice();
                final ShiftBar shiftBar = bar.withShift();
                final Human human = presenter.getHuman();
                final Presentation presentation = ui.present(human, shiftBar, presenter);
                final float padding = padlet.toFloat(human, presentation.getWidth());
                shiftBar.doShift(padding, 0);
                presenter.addPresentation(
                      new ResizePresentation(padding + presentation.getWidth(), presentation.getHeight(),
                                             presentation));
            }
        });
    }

    public static Span0 create(final OnPresent<Bar> onPresent) {
        return new Span0() {
            @Override
            public Presentation present(Human human, final Bar bar, Observer observer) {
                final BasePresenter<Bar> presenter = new BasePresenter<Bar>(human, bar, observer) {
                    @Override
                    public float getWidth() {
                        float union = 0;
                        for (Presentation presentation : presentations) {
                            union = Math.max(union, presentation.getWidth());
                        }
                        return union;
                    }

                    @Override
                    public float getHeight() {
                        return device.fixedHeight;
                    }
                };
                onPresent.onPresent(presenter);
                return presenter;
            }
        };
    }
}

