package com.rubyhuntersky.gx.uis;

import com.rubyhuntersky.gx.internal.presenters.Presenter;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public interface OnPresent<T> {
    void onPresent(Presenter<T> presenter);
}
