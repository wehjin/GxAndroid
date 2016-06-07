package com.rubyhuntersky.gx.internal.devices;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.internal.devices.screen.Screen;

/**
 * @author wehjin
 * @since 1/28/16.
 */

public interface Device<T> extends Screen {
    @NonNull
    T toType();

    @NonNull
    T withElevation(int elevation);

    @NonNull
    DelayDevice<T> withDelay();

    @NonNull
    ShiftDevice<T> withShift();
}
