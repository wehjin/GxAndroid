package com.rubyhuntersky.gx.tiles;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.internal.devices.CoreDevice;
import com.rubyhuntersky.gx.internal.devices.DelayDevice;
import com.rubyhuntersky.gx.internal.devices.PatchDevice;
import com.rubyhuntersky.gx.internal.devices.PatchDeviceWrapper;

/**
 * @author wehjin
 * @since 1/28/16.
 */

public class Mosaic extends PatchDeviceWrapper implements CoreDevice<Mosaic> {

    public final float relatedWidth;
    public final float relatedHeight;
    public final int elevation;

    public Mosaic(float relatedWidth, float relatedHeight, int elevation, PatchDevice patchDevice) {
        super(patchDevice);
        this.relatedWidth = relatedWidth;
        this.relatedHeight = relatedHeight;
        this.elevation = elevation;
    }

    protected Mosaic(Mosaic basis) {
        this(basis.relatedWidth, basis.relatedHeight, basis.elevation, basis);
    }

    @NonNull
    @Override
    public ShiftMosaic withShift() {
        return new ShiftMosaic(this);
    }

    @NonNull
    @Override
    public DelayDevice<Mosaic> withDelay() {
        return null;
    }

    @NonNull
    @Override
    public Mosaic withElevation(int elevation) {
        return elevation == this.elevation ? this : new Mosaic(relatedWidth, relatedHeight, elevation, this);
    }

    @NonNull
    @Override
    public Mosaic asType() {
        return this;
    }

}
