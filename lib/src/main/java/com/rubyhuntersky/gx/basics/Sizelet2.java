package com.rubyhuntersky.gx.basics;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public class Sizelet2 {

    public static final Sizelet2 ZERO = new Sizelet2(Sizelet.ZERO, Sizelet.ZERO);
    private final Sizelet origin;
    private final Sizelet size;

    public Sizelet2(Sizelet origin, Sizelet size) {
        this.origin = origin;
        this.size = size;
    }
}
