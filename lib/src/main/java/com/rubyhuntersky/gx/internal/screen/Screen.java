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

public interface Screen {
    @NonNull
    Patch addPatch(@NonNull Frame frame, @NonNull Shape shape, int argbColor);

    @NonNull
    TextSize measureText(@NonNull String text, @NonNull TextStyle textStyle);

    @NonNull
    ShapeSize measureShape(@NonNull Shape shape);

    @NonNull
    Removable addSurface(@NonNull Frame frame, @NonNull Jester jester);

    @NonNull
    Presentation present(@NonNull Div0 div, @NonNull Observer observer);
}
