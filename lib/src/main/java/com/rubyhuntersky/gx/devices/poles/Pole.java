package com.rubyhuntersky.gx.devices.poles;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.internal.devices.FixedDimensionDevice;
import com.rubyhuntersky.gx.internal.devices.screen.Screen;
import com.rubyhuntersky.gx.internal.devices.screen.ScreenChain;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public class Pole extends ScreenChain implements FixedDimensionDevice<Pole> {

    public final float fixedWidth;
    public final float relatedHeight;
    public final int elevation;

    public Pole(float fixedWidth, float relatedHeight, int elevation, Screen screen) {
        super(screen);
        this.fixedWidth = fixedWidth;
        this.relatedHeight = relatedHeight;
        this.elevation = elevation;
    }

    protected Pole(@NonNull Pole original) {
        this(original.fixedWidth, original.relatedHeight, original.elevation, original);
    }

    public Pole withFixedWidth(float fixedWidth) {
        return new Pole(fixedWidth, relatedHeight, elevation, this);
    }

    public Pole withShift(float horizontal, float vertical) {
        final ShiftPole frameShiftColumn = withShift();
        frameShiftColumn.doShift(horizontal, vertical);
        return frameShiftColumn;
    }

    @NonNull
    public ShiftPole withShift() {
        return new ShiftPole(this);
    }

    @NonNull
    @Override
    public DelayPole withDelay() {
        return new DelayPole(this);
    }

    @NonNull
    public Pole withElevation(int elevation) {
        return elevation == this.elevation ? this : new Pole(fixedWidth, relatedHeight, elevation, this);
    }

    public Pole withRelatedHeight(float relatedHeight) {
        return relatedHeight == this.relatedHeight ? null : new Pole(fixedWidth, relatedHeight, elevation, this);
    }

    @NonNull
    @Override
    public Pole withFixedDimension(float fixedDimension) {
        return withFixedWidth(fixedDimension);
    }

    @NonNull
    @Override
    public Pole toType() {
        return this;
    }
}
