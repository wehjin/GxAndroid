package com.rubyhuntersky.gx.presentations;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.observers.Observer;
import com.rubyhuntersky.gx.uis.core.Ui0;

/**
 * @author wehjin
 * @since 1/30/16.
 */

public class MultiDevicePresentation<T> extends BooleanPresentation implements Presentation {

    private final Ui0<T> ui;
    private final Human human;
    private final Observer observer;
    private Presentation presentation = new EmptyPresentation();

    public MultiDevicePresentation(Ui0<T> ui, Human human, T device, Observer observer) {
        this.ui = ui;
        this.human = human;
        this.observer = observer;
        setDevice(device);
    }

    public MultiDevicePresentation() {
        this.ui = null;
        this.human = null;
        this.observer = null;
    }

    public void setDevice(T device) {
        if (isCancelled()) {
            return;
        }
        presentation.cancel();
        presentation = device == null ? new EmptyPresentation() : ui.present(human, device, observer);
    }

    @Override
    public float getWidth() {
        return isCancelled() ? 0 : presentation.getWidth();
    }

    @Override
    public float getHeight() {
        return isCancelled() ? 0 : presentation.getHeight();
    }

    @Override
    protected void onCancel() {
        presentation.cancel();
    }
}
