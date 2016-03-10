package com.rubyhuntersky.gx.internal.presenters;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.observers.Observer;
import com.rubyhuntersky.gx.presentations.Presentation;

import java.util.List;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public interface Presenter<T> extends Observer, Presentation {

    Human getHuman();
    T getDevice();
    List<Presentation> getPresentations();
    void addPresentation(Presentation presentation);
}