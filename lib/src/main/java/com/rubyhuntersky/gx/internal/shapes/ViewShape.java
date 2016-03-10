package com.rubyhuntersky.gx.internal.shapes;

import android.content.Context;
import android.view.View;

/**
 * @author wehjin
 * @since 1/23/16.
 */

abstract public class ViewShape extends Shape {
    public abstract View createView(Context context);
}
