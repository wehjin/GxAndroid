package com.rubyhuntersky.gx.basics;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public class Frame {
    public final Range horizontal;
    public final Range vertical;
    public final int elevation;

    public Frame(float width, float height, int elevation) {
        this.horizontal = Range.of(0, width);
        this.vertical = Range.of(0, height);
        this.elevation = elevation;
    }

    public Frame(Range horizontal, Range vertical, int elevation) {
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.elevation = elevation;
    }

    @Override
    public String toString() {
        return "Frame{" +
              "horizontal=" + horizontal +
              ", vertical=" + vertical +
              ", elevation=" + elevation +
              '}';
    }

    public Frame withVerticalShift(float shift) {
        Range newVertical = vertical.shift(shift);
        if (newVertical == vertical) {
            return this;
        }
        return new Frame(horizontal, newVertical, elevation);
    }

    public Frame withShift(float horizontalShift, float verticalShift) {
        Range newVertical = vertical.shift(verticalShift);
        Range newHorizontal = horizontal.shift(horizontalShift);
        if (newVertical == vertical && newHorizontal == horizontal) {
            return this;
        }
        return new Frame(newHorizontal, newVertical, elevation);
    }

    public Frame withVertical(Range range) {
        return new Frame(horizontal, range, elevation);
    }

    public Frame withVerticalLength(float verticalLength) {
        final Range newVertical = vertical.withLength(verticalLength);
        return new Frame(horizontal, newVertical, elevation);
    }
}
