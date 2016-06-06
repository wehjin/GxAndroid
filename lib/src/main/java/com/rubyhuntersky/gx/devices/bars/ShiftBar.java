package com.rubyhuntersky.gx.devices.bars;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.basics.Frame;
import com.rubyhuntersky.gx.internal.devices.ShiftDevice;
import com.rubyhuntersky.gx.internal.patches.FrameShiftPatch;
import com.rubyhuntersky.gx.internal.patches.Patch;
import com.rubyhuntersky.gx.internal.shapes.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wehjin
 * @since 1/24/16.
 */
public class ShiftBar extends Bar implements ShiftDevice<Bar> {

    private boolean didShift;
    private List<FrameShiftPatch> pending = new ArrayList<>();
    private float verticalShift;
    private float horizontalShift;

    public ShiftBar(@NonNull Bar original) {
        super(original);
    }

    @NonNull
    @Override
    public ShiftBar doShift(float horizontalShift, float verticalShift) {
        if (!didShift) {
            didShift = true;
            this.horizontalShift = horizontalShift;
            this.verticalShift = verticalShift;
            final List<FrameShiftPatch> toShift = new ArrayList<>(pending);
            pending.clear();
            for (FrameShiftPatch patch : toShift) {
                patch.setShift(horizontalShift, verticalShift);
            }
        }
        return this;
    }

    @NonNull
    @Override
    public Patch addPatch(Frame frame, Shape shape, int argbColor) {
        final FrameShiftPatch patch = new FrameShiftPatch(frame, shape, argbColor, basis);
        if (didShift) {
            patch.setShift(horizontalShift, verticalShift);
        } else {
            pending.add(patch);
        }
        return patch;
    }
}