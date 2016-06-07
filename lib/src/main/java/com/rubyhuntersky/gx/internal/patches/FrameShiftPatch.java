package com.rubyhuntersky.gx.internal.patches;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.basics.Frame;
import com.rubyhuntersky.gx.internal.devices.screen.Screen;
import com.rubyhuntersky.gx.internal.shapes.Shape;

/**
 * @author wehjin
 * @since 1/24/16.
 */
public class FrameShiftPatch implements Patch {

    private final Frame frame;
    private final Shape shape;
    private final int argbColor;
    private final Screen device;
    private Patch patch;
    private boolean didShift;
    private boolean didRemove;

    public FrameShiftPatch(Frame frame, Shape shape, int argbColor, @NonNull Screen device) {
        this.frame = frame;
        this.shape = shape;
        this.argbColor = argbColor;
        this.device = device;
    }

    public void setShift(float horizontal, float vertical) {
        if (didRemove || didShift) {
            return;
        }
        didShift = true;
        patch = device.addPatch(frame.withShift(horizontal, vertical), shape, argbColor);
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
