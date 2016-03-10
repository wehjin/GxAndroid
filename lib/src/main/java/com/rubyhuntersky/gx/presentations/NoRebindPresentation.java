package com.rubyhuntersky.gx.presentations;

/**
 * @author wehjin
 * @since 1/30/16.
 */
public class NoRebindPresentation implements Presentation {
    private final Presentation present;

    public NoRebindPresentation(Presentation present) {
        this.present = present;
    }

    @Override
    public float getWidth() {
        return present.getWidth();
    }

    @Override
    public float getHeight() {
        return present.getHeight();
    }

    @Override
    public boolean isCancelled() {
        return present.isCancelled();
    }

    @Override
    public void cancel() {
        present.cancel();
    }
}
