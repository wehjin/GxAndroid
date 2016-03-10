package com.rubyhuntersky.gx.basics;

import android.graphics.Typeface;

import com.rubyhuntersky.coloret.Coloret;
import com.rubyhuntersky.gx.Human;

import static com.rubyhuntersky.coloret.Coloret.DARK_TEXT;
import static com.rubyhuntersky.gx.basics.Sizelet.IMPORTANT;
import static com.rubyhuntersky.gx.basics.Sizelet.READABLE;
import static com.rubyhuntersky.gx.basics.Sizelet.TITLE;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public class TextStylet {
    public static final TextStylet TITLE_DARK = new TextStylet(TITLE, Typeface.DEFAULT, DARK_TEXT);
    public static final TextStylet IMPORTANT_DARK = new TextStylet(IMPORTANT, Typeface.DEFAULT, DARK_TEXT);
    public static final TextStylet READABLE_DARK = new TextStylet(READABLE, Typeface.DEFAULT, DARK_TEXT);

    public final Sizelet heightlet;
    public final Typeface typeface;
    public final Coloret coloret;

    public TextStylet(Sizelet heightlet, Typeface typeface, Coloret coloret) {
        this.heightlet = heightlet;
        this.typeface = typeface;
        this.coloret = coloret;
    }

    public TextStyle toStyle(Human human, float contextSize) {
        return new TextStyle(heightlet.toFloat(human, contextSize), typeface, coloret.toArgb());
    }
}
