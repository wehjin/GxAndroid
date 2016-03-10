package com.rubyhuntersky.gx.basics;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public class Range {

    public static final Range ZERO = new Range(0, 0);

    public static Range of(float start, float end) {
        return new Range(start, end);
    }

    public final float start;
    public final float end;

    public Range(float start, float end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "Range{" +
              "start=" + start +
              ", end=" + end +
              '}';
    }

    public float toLength() {
        return end - start;
    }

    public Range union(Range range) {
        float newStart = Math.min(start, range.start);
        float newEnd = Math.max(end, range.end);
        if (isEqual(newStart, newEnd)) {
            return this;
        } else if (range.isEqual(newStart, newEnd)) {
            return range;
        } else {
            return new Range(newStart, newEnd);
        }
    }

    public Range inset(float amount) {
        if (amount == 0) {
            return this;
        }
        return new Range(start + amount, end - amount);
    }

    public Range outset(float amount) {
        if (amount == 0) {
            return this;
        }
        return new Range(start - amount, end + amount);
    }

    public Range moveStart(float amount) {
        if (amount == 0) {
            return this;
        }
        return new Range(start + amount, end);
    }

    public Range moveEnd(float amount) {
        if (amount == 0) {
            return this;
        }
        return new Range(start, end + amount);
    }

    public Range shift(float shift) {
        if (shift == 0) {
            return this;
        }
        return new Range(start + shift, end + shift);
    }

    public Range withLength(float length) {
        return new Range(start, start + length);
    }

    private boolean isEqual(float start, float end) {
        return this.start == start && this.end == end;
    }
}
