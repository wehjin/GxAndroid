package com.rubyhuntersky.gx.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.rubyhuntersky.gx.presentations.Presentation;
import com.rubyhuntersky.gx.devices.poles.Pole;
import com.rubyhuntersky.gx.internal.devices.DelayDevice;
import com.rubyhuntersky.gx.internal.devices.ShiftDevice;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public class PoleView extends FixedDimensionDeviceView<Pole> {

    public static final String TAG = PoleView.class.getSimpleName();
    private Pole pole;

    public PoleView(Context context) {
        super(context);
        init();
    }

    public PoleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PoleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        pole = new Pole(0, 0, 0, this);
    }

    @NonNull
    @Override
    public Pole toType() {
        return pole;
    }

    @NonNull
    @Override
    public Pole withFixedDimension(float fixedDimension) {
        return pole.withFixedDimension(fixedDimension);
    }

    @NonNull
    @Override
    public DelayDevice<Pole> withDelay() {
        return pole.withDelay();
    }

    @NonNull
    @Override
    public ShiftDevice<Pole> withShift() {
        return pole.withShift();
    }

    @NonNull
    @Override
    public Pole withElevation(int elevation) {
        return pole.withElevation(elevation);
    }

    @Override
    protected int getFixedDimensionMeasureSpec(int widthMeasureSpec, int heightMeasureSpec) {
        return widthMeasureSpec;
    }

    @Override
    protected void setMeasuredDimensionFromDeviceDimensions(float fixed, float variable) {
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
