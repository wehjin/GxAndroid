package com.rubyhuntersky.gx.android;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.rubyhuntersky.gx.internal.shapes.Shape;
import com.rubyhuntersky.gx.basics.ShapeSize;
import com.rubyhuntersky.gx.internal.shapes.ViewShape;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @author wehjin
 * @since 2/11/16.
 */

public class ShapeRuler {
    private final Context context;

    public ShapeRuler(Context context) {

        this.context = context;
    }

    public ShapeSize measure(Shape shape) {
        if (shape instanceof ViewShape) {
            final ViewShape viewShape = (ViewShape) shape;
            final View view = viewShape.createView(context);
            view.setLayoutParams(new FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            final int measuredWidth = view.getMeasuredWidth();
            final int measuredHeight = view.getMeasuredHeight();
            return new ShapeSize(measuredWidth, measuredHeight);
        } else {
            return ShapeSize.ZERO;
        }
    }
}
