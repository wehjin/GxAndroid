package com.rubyhuntersky.gx.devices.poles;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.basics.Frame;
import com.rubyhuntersky.gx.basics.ShapeSize;
import com.rubyhuntersky.gx.basics.TextSize;
import com.rubyhuntersky.gx.basics.TextStyle;
import com.rubyhuntersky.gx.internal.patches.Patch;
import com.rubyhuntersky.gx.internal.shapes.Shape;

/**
 * @author wehjin
 * @since 1/29/16.
 */

abstract public class SeedPole extends Pole {
    public SeedPole(float fixedWidth, float relatedHeight, int elevation) {
        super(fixedWidth, relatedHeight, elevation, null);
    }

    @NonNull
    @Override
    abstract public Patch addPatch(@NonNull Frame frame, @NonNull Shape shape, int argbColor);

    @NonNull
    @Override
    abstract public TextSize measureText(@NonNull String text, @NonNull TextStyle textStyle);

    @NonNull
    @Override
    abstract public ShapeSize measureShape(@NonNull Shape shape);
}
