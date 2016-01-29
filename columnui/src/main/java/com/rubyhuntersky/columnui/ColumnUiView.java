package com.rubyhuntersky.columnui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.rubyhuntersky.columnui.basics.Frame;
import com.rubyhuntersky.columnui.basics.TextSize;
import com.rubyhuntersky.columnui.basics.TextStyle;
import com.rubyhuntersky.columnui.columns.Column;
import com.rubyhuntersky.columnui.displays.DelayDisplay;

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
        column = new Column(0, 0, 0) {

            @NonNull
            @Override
            public Patch addPatch(Frame frame, Shape shape) {
                return ColumnUiView.this.addPatch(frame, shape);
            }

            @NonNull
            @Override
            public TextSize measureText(String text, TextStyle textStyle) {
                return ColumnUiView.this.measureText(text, textStyle);
            }
        };
    }

    @NonNull
    @Override
    public Column asType() {
        return column;
    }

    @NonNull
    @Override
    public Column withFixedDimension(float fixedDimension) {
        return column.withFixedDimension(fixedDimension);
    }

    @NonNull
    @Override
    public DelayDisplay<Column> withDelay() {
        return column.withDelay();
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