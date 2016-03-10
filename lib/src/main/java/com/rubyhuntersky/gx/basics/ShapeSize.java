package com.rubyhuntersky.gx.basics;

/**
 * @author wehjin
 * @since 1/29/16.
 */

public class ShapeSize {
    public static final ShapeSize ZERO = new ShapeSize(0, 0);
    public final int measuredWidth;
    public final int measuredHeight;

    public ShapeSize(int measuredWidth, int measuredHeight) {
        this.measuredWidth = measuredWidth;
        this.measuredHeight = measuredHeight;
    }

    @Override
    public String toString() {
        return "ShapeSize{" +
              "measuredWidth=" + measuredWidth +
              ", measuredHeight=" + measuredHeight +
              '}';
    }
}
