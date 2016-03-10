package com.rubyhuntersky.gx.internal.devices;

import android.support.annotation.NonNull;

/**
 * @author wehjin
 * @since 1/27/16.
 */

public interface FixedDimensionDevice<T> extends Device<T> {

    @NonNull
    T withFixedDimension(float fixedDimension);
}
