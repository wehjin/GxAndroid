package com.rubyhuntersky.columnui.ui;

/**
 * @author wehjin
 * @since 1/30/16.
 */

public interface Ui1<T, C> {

    BoundUi1<T, C> bind(C condition);
}