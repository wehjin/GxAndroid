package com.rubyhuntersky.gx.basics;

/**
 * @author wehjin
 * @since 1/24/16.
 */

public class TextHeight {

    public static final TextHeight ZERO = new TextHeight(0, 0);
    public final float height;
    public final float topPadding;

    public TextHeight(float height, float topPadding) {
        this.height = height;
        this.topPadding = topPadding;
    }

}
