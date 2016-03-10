package com.rubyhuntersky.gx.basics;

/**
 * @author wehjin
 * @since 1/24/16.
 */

public class TextSize {
    public static final TextSize ZERO = new TextSize(0, TextHeight.ZERO);
    public final float textWidth;
    public final TextHeight textHeight;

    public TextSize(float textWidth, TextHeight textHeight) {
        this.textWidth = textWidth;
        this.textHeight = textHeight;
    }
}
