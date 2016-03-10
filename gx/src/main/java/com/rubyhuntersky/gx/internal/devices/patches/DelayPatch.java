package com.rubyhuntersky.gx.internal.devices.patches;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.basics.Frame;
import com.rubyhuntersky.gx.internal.devices.PatchDevice;
import com.rubyhuntersky.gx.internal.shapes.Shape;

/**
 * @author wehjin
 * @since 1/24/16.
 */
public class DelayPatch implements Patch {

    private final Frame frame;
    private final Shape shape;
    private final int argbColor;
    private final PatchDevice display;
    private Patch patch;
    private boolean didEndDelay;
    private boolean didRemove;

    public DelayPatch(Frame frame, Shape shape, int argbColor, @NonNull PatchDevice display) {

        this.frame = frame;
        this.shape = shape;
        this.argbColor = argbColor;
        this.display = display;
    }

    public void endDelay() {
        if (didRemove || didEndDelay) {
            return;
        }
        patch = display.addPatch(frame, shape, argbColor);
        didEndDelay = true;
    }

    @Override
    public void remove() {
        if (didRemove) {
            return;
        }
        didRemove = true;
        if (patch != null) {
            patch.remove();
            patch = null;
        }
    }
}
