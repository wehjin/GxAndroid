package com.rubyhuntersky.gx.uis.divs;

import com.rubyhuntersky.gx.basics.Frame;
import com.rubyhuntersky.gx.basics.Removable;
import com.rubyhuntersky.gx.basics.Spot;
import com.rubyhuntersky.gx.devices.poles.Pole;
import com.rubyhuntersky.gx.internal.surface.Jester;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Jeffrey Yu
 * @since 6/14/16.
 */
public class TapPresenterPresentation<T> extends Div.PresenterPresentation {

    private final T name;
    private final Div.Presenter presenter;
    private Div.Presentation presentation;
    private Removable surface;

    public TapPresenterPresentation(Div0 upstream, T name, Div.Presenter presenter) {
        super(presenter);
        this.name = name;
        this.presenter = presenter;
        presentation = upstream.present(getHuman(), getPole(), new Div.ForwardingObserver(presenter) {
            @Override
            public void onHeight(float height) {
                updateSurface(height);
                super.onHeight(height);
            }
        });

    }

    private void updateSurface(float height) {
        if (surface != null) {
            surface.remove();
        }
        final Pole pole = getPole();
        surface = pole.addSurface(new Frame(pole.getWidth(), height, pole.getElevation()),
                                  new Jester() {
                                      @Nullable
                                      @Override
                                      public Contact getContact(@NotNull final Spot downSpot) {
                                          return new TapContact<>(downSpot, presenter, presenter.getHuman(), name);
                                      }
                                  });
    }

    @Override
    public void onCancel() {
        if (surface != null) {
            surface.remove();
        }
        presentation.cancel();
    }
}
