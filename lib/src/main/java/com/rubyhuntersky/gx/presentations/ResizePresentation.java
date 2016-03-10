package com.rubyhuntersky.gx.presentations;

/**
 * @author wehjin
 * @since 1/24/16.
 */
public class ResizePresentation implements Presentation {

    private final float width;
    private final float height;
    private final Presentation original;

    public ResizePresentation(float width, float height, Presentation original) {
        this.width = width;
        this.height = height;
        this.original = original;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public boolean isCancelled() {
        return original.isCancelled();
    }

    @Override
    public void cancel() {
        original.cancel();
    }
}
