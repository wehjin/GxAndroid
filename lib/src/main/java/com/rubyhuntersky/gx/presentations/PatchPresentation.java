package com.rubyhuntersky.gx.presentations;

import com.rubyhuntersky.gx.basics.Frame;
import com.rubyhuntersky.gx.internal.patches.Patch;

/**
 * @author wehjin
 * @since 1/28/16.
 */
public class PatchPresentation extends BooleanPresentation {
    private final Frame frame;
    private final Patch patch;

    public PatchPresentation(Patch patch, Frame frame) {
        this.frame = frame;
        this.patch = patch;
    }

    @Override
    public float getWidth() {
        return frame.getHorizontal().toLength();
    }

    @Override
    public float getHeight() {
        return frame.getVertical().toLength();
    }

    @Override
    protected void onCancel() {
        patch.remove();
    }
}
