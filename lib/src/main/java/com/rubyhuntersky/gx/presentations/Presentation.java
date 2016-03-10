package com.rubyhuntersky.gx.presentations;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public interface Presentation {
    Presentation EMPTY = new EmptyPresentation();

    float getWidth();
    float getHeight();
    boolean isCancelled();
    void cancel();
}
