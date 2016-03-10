package com.rubyhuntersky.gx.uis.core;

/**
 * @author wehjin
 * @since 1/30/16.
 */

public interface Ui2<T, C1, C2> {

    Ui1<T, C2> bind(C1 condition);
}
