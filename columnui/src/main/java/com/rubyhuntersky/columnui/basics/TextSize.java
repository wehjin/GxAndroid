package com.rubyhuntersky.columnui.basics;

/**
 * @author wehjin
 * @since 1/24/16.
 */

public class TextSize {
    public static final TextSize ZERO = new TextSize(0, TextHeight.ZERO);
    public final float width;
    public final TextHeight textHeight;

    public TextSize(float width, TextHeight textHeight) {
        this.width = width;
        this.textHeight = textHeight;
    }

}
