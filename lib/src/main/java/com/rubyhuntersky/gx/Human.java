package com.rubyhuntersky.gx;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public class Human {
    final public float fingerPixels;
    final public float textPixels;

    public Human(float fingerPixels, float textPixels) {
        this.fingerPixels = fingerPixels;
        this.textPixels = textPixels;
    }

    @Override
    public String toString() {
        return "Human{" +
              "fingerPixels=" + fingerPixels +
              ", textPixels=" + textPixels +
              '}';
    }
}
