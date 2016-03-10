package com.rubyhuntersky.gx.devices.bars;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.basics.Frame;
import com.rubyhuntersky.gx.internal.devices.DelayDevice;
import com.rubyhuntersky.gx.internal.patches.DelayPatch;
import com.rubyhuntersky.gx.internal.patches.Patch;
import com.rubyhuntersky.gx.internal.shapes.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wehjin
 * @since 1/24/16.
 */
public class DelayBar extends Bar implements DelayDevice<Bar> {

    protected boolean didEndDelay;
    private List<DelayPatch> pending = new ArrayList<>();

    public DelayBar(@NonNull Bar original) {
        super(original);
    }

    public void endDelay() {
        if (didEndDelay) {
            return;
        }
        didEndDelay = true;
        final List<DelayPatch> toEndDelay = new ArrayList<>(pending);
        pending.clear();
        for (DelayPatch patch : toEndDelay) {
            patch.endDelay();
        }
    }

    @NonNull
    @Override
    public Patch addPatch(Frame frame, Shape shape, int argbColor) {
        if (didEndDelay) {
            return basis.addPatch(frame, shape, argbColor);
        }
        final DelayPatch patch = new DelayPatch(frame, shape, argbColor, basis);
        pending.add(patch);
        return patch;
    }
}
