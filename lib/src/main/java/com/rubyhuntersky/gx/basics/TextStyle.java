package com.rubyhuntersky.gx.basics;

import android.graphics.Typeface;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public class TextStyle {
    public final int typeheight;
    public final Typeface typeface;
    public final int typecolor;

    public TextStyle(float typeheight, Typeface typeface, int typecolor) {
        this.typeheight = (int) typeheight;
        this.typeface = typeface;
        this.typecolor = typecolor;
    }

    @Override
    public String toString() {
        return "TextStyle{" +
              "typeheight=" + typeheight +
              ", typeface=" + typeface +
              ", typecolor=" + typecolor +
              '}';
    }
}
