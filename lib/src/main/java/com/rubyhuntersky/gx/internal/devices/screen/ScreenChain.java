package com.rubyhuntersky.gx.internal.devices.screen;

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
public class ScreenChain implements Screen {
    final protected Screen basis;

    public ScreenChain(Screen basis) {
        this.basis = basis;
    }

    @NonNull
    @Override
    public Patch addPatch(@NonNull Frame frame, @NonNull Shape shape, int argbColor) {
        return basis.addPatch(frame, shape, argbColor);
    }

    @NonNull
    @Override
    public TextSize measureText(@NonNull String text, @NonNull TextStyle textStyle) {
        return basis.measureText(text, textStyle);
    }

    @NonNull
    @Override
    public ShapeSize measureShape(@NonNull Shape shape) {
        return basis.measureShape(shape);
    }
}
