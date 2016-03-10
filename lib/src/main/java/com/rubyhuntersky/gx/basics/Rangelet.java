package com.rubyhuntersky.gx.basics;

import com.rubyhuntersky.gx.Human;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public class Rangelet {

    public final Sizelet startlet;
    public final Sizelet endlet;

    public Rangelet(Sizelet startlet, Sizelet endlet) {
        this.startlet = startlet;
        this.endlet = endlet;
    }

    public Range toRange(Human human, float factor) {
        final float start = startlet.toFloat(human, factor);
        final float end = endlet.toFloat(human, factor);
        return new Range(start, end);
    }
}
