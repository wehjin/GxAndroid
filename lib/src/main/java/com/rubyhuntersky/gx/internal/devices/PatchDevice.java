package com.rubyhuntersky.gx.internal.devices;

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

public interface PatchDevice {
    @NonNull
    Patch addPatch(Frame frame, Shape shape, int argbColor);

    @NonNull
    TextSize measureText(String text, TextStyle textStyle);

    @NonNull
    ShapeSize measureShape(Shape shape);
}
