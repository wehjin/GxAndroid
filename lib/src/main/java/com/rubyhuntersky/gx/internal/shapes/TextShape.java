package com.rubyhuntersky.gx.internal.shapes;

import com.rubyhuntersky.gx.basics.TextSize;
import com.rubyhuntersky.gx.basics.TextStyle;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public class TextShape extends Shape {
    public final String textString;
    public final TextStyle textStyle;
    public final TextSize textSize;

    public TextShape(String textString, TextStyle textStyle, TextSize textSize) {
        this.textString = textString;
        this.textStyle = textStyle;
        this.textSize = textSize;
    }
}
