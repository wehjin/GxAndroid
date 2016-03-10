package com.rubyhuntersky.gx.internal.devices;

import android.support.annotation.NonNull;

/**
 * @author wehjin
 * @since 1/24/16.
 */
public interface ShiftDevice<T> extends Device<T> {

    @NonNull
    ShiftDevice<T> doShift(float horizontal, float vertical);
}
