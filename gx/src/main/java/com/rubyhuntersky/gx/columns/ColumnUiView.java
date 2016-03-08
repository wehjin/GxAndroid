package com.rubyhuntersky.gx.columns;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.rubyhuntersky.gx.displays.DelayDisplay;
import com.rubyhuntersky.gx.displays.ShiftDisplay;
import com.rubyhuntersky.gx.presentations.Presentation;
import com.rubyhuntersky.gx.ui.UiView;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public class ColumnUiView extends UiView<Column> {

    public static final String TAG = ColumnUiView.class.getSimpleName();
    private Column column;

    public ColumnUiView(Context context) {
        super(context);
        init();
    }

    public ColumnUiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColumnUiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        column = new Column(0, 0, 0, this);
    }

    @NonNull
    @Override
    public Column asType() {
        return column;
    }

    @NonNull
    @Override
    public Column asDisplayWithFixedDimension(float fixedDimension) {
        return column.asDisplayWithFixedDimension(fixedDimension);
    }

    @NonNull
    @Override
    public DelayDisplay<Column> withDelay() {
        return column.withDelay();
    }

    @NonNull
    @Override
    public ShiftDisplay<Column> withShift() {
        return column.withShift();
    }

    @NonNull
    @Override
    public Column withElevation(int elevation) {
        return column.withElevation(elevation);
    }

    @Override
    protected int getFixedDimensionMeasureSpec(int widthMeasureSpec, int heightMeasureSpec) {
        return widthMeasureSpec;
    }

    @Override
    protected void setMeasuredDimensionFromDisplayDimensions(float fixed, float variable) {
        setMeasuredDimension((int) fixed, (int) variable);
    }

    @Override
    protected float getVariableDimension(Presentation presentation) {
        return presentation.getHeight();
    }

    @Override
    protected float getFixedDimension(int width, int height) {
        return width;
    }
}
