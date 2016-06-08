package com.rubyhuntersky.gx.internal.screen;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.basics.Frame;
import com.rubyhuntersky.gx.basics.Removable;
import com.rubyhuntersky.gx.basics.ShapeSize;
import com.rubyhuntersky.gx.basics.TextSize;
import com.rubyhuntersky.gx.basics.TextStyle;
import com.rubyhuntersky.gx.internal.patches.Patch;
import com.rubyhuntersky.gx.internal.shapes.Shape;
import com.rubyhuntersky.gx.internal.surface.Jester;
import com.rubyhuntersky.gx.observers.Observer;
import com.rubyhuntersky.gx.presentations.Presentation;
import com.rubyhuntersky.gx.uis.divs.Div0;

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

    @NonNull
    @Override
    public Removable addSurface(@NonNull Frame frame, @NonNull Jester jester) {
        return basis.addSurface(frame, jester);
    }

    @NonNull
    @Override
    public Presentation present(@NonNull Div0 div, @NonNull Observer observer) {
        return basis.present(div, observer);
    }
}
