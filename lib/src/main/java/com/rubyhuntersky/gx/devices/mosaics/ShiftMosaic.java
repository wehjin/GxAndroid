package com.rubyhuntersky.gx.devices.mosaics;

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
public class ShiftMosaic extends Mosaic implements ShiftDevice<Mosaic> {

    private boolean didShift;
    private List<FrameShiftPatch> pending = new ArrayList<>();
    private float verticalShift;
    private float horizontalShift;

    public ShiftMosaic(@NonNull Mosaic original) {
        super(original);
    }

    @NonNull
    @Override
    public ShiftMosaic doShift(float horizontal, float vertical) {
        if (!didShift) {
            didShift = true;
            this.horizontalShift = horizontal;
            this.verticalShift = vertical;
            final List<FrameShiftPatch> toShift = new ArrayList<>(pending);
            pending.clear();
            for (FrameShiftPatch patch : toShift) {
                patch.setShift(horizontal, vertical);
            }
        }
        return this;
    }

    @NonNull
    @Override
    public Patch addPatch(@NonNull Frame frame, @NonNull Shape shape, int argbColor) {
        final FrameShiftPatch patch = new FrameShiftPatch(frame, shape, argbColor, basis);
        if (didShift) {
            patch.setShift(horizontalShift, verticalShift);
        } else {
            pending.add(patch);
        }
        return patch;
    }
}
