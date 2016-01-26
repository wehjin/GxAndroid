package com.rubyhuntersky.columnui.conditions;

import android.support.annotation.NonNull;

import com.rubyhuntersky.columnui.Coloret;
import com.rubyhuntersky.columnui.Frame;
import com.rubyhuntersky.columnui.Patch;
import com.rubyhuntersky.columnui.Shape;

/**
 * @author wehjin
 * @since 1/24/16.
 */
public class DelayPatch implements Patch {

    private final Frame frame;
    private final Shape shape;
    private final Coloret color;
    private final Column column;
    private Patch patch;

    public DelayPatch(Frame frame, Shape shape, Coloret color, @NonNull Column column) {

        this.frame = frame;
        this.shape = shape;
        this.color = color;
        this.column = column;
    }

    public void endDelay() {
        if (patch != null) {
            throw new IllegalStateException("Delay already ended");
        }
        patch = column.addPatch(frame, shape, color);
    }

    @Override
    public void remove() {
        if (patch != null) {
            patch.remove();
            patch = null;
        }
    }
}