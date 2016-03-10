package com.rubyhuntersky.gx.basics;

import com.rubyhuntersky.gx.Human;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public class Sizelet {
    public static final Sizelet FINGER = new Sizelet(1, Ruler.FINGERTIP);
    public static final Sizelet TWO_THIRDS_FINGER = new Sizelet(.6666f, Ruler.FINGERTIP);
    public static final Sizelet HALF_FINGER = new Sizelet(.5f, Ruler.FINGERTIP);
    public static final Sizelet THIRD_FINGER = new Sizelet(.3333f, Ruler.FINGERTIP);
    public static final Sizelet QUARTER_FINGER = new Sizelet(.25f, Ruler.FINGERTIP);
    public static final Sizelet READABLE = new Sizelet(1f, Ruler.READABLE);
    public static final Sizelet TITLE = new Sizelet(1.8f, Ruler.READABLE);
    public static final Sizelet IMPORTANT = new Sizelet(1.4f, Ruler.READABLE);
    public static final Sizelet PREVIOUS = new Sizelet(1, Ruler.PREVIOUS);

    public static Sizelet ZERO = new Sizelet(0, Ruler.ZERO);
    public static Sizelet FULL = new Sizelet(1, Ruler.PREVIOUS);

    public static Sizelet pixels(int pixels) {
        return new Sizelet((float) pixels, Ruler.UNIT);
    }

    public static Sizelet readables(float count) {
        return new Sizelet(count, Ruler.READABLE);
    }

    private final float portion;

    private final Ruler ruler;

    public Sizelet(float portion, Ruler ruler) {
        this.portion = portion;
        this.ruler = ruler;
    }

    protected Sizelet() {
        portion = 0;
        ruler = Ruler.ZERO;
    }

    public float toFloat(Human human, float containerSize) {
        float fullSize;
        if (ruler == Ruler.FINGERTIP) {
            fullSize = human.fingerPixels;
        } else if (ruler == Ruler.READABLE) {
            fullSize = human.textPixels;
        } else if (ruler == Ruler.PREVIOUS) {
            fullSize = containerSize;
        } else if (ruler == Ruler.UNIT) {
            fullSize = 1;
        } else {
            fullSize = 0;
        }
        return portion * fullSize;
    }

    public Sizelet twice() {
        final Sizelet previous = this;
        return new Sizelet() {
            @Override
            public float toFloat(Human human, float containerSize) {
                return previous.toFloat(human, containerSize) * 2;
            }
        };
    }

    public enum Ruler {
        ZERO,
        FINGERTIP,
        READABLE,
        PREVIOUS,
        UNIT
    }
}
