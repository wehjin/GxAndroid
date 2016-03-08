package com.rubyhuntersky.gx.displays;

import android.support.annotation.NonNull;

/**
 * @author wehjin
 * @since 1/27/16.
 */

public interface FixedDisplay<T> extends CoreDisplay<T> {

    @NonNull
    T asDisplayWithFixedDimension(float fixedDimension);
}
