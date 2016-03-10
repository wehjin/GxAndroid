package com.rubyhuntersky.gx.uis.core;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.observers.Observer;
import com.rubyhuntersky.gx.presentations.Presentation;

/**
 * @author wehjin
 * @since 1/27/16.
 */

public interface Ui0<T> {
    Presentation present(Human human, T device, Observer observer);
}