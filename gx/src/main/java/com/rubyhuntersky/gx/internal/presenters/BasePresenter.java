package com.rubyhuntersky.gx.internal.presenters;

import com.rubyhuntersky.gx.client.Human;
import com.rubyhuntersky.gx.client.Observer;
import com.rubyhuntersky.gx.client.Presentation;
import com.rubyhuntersky.gx.client.Reaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wehjin
 * @since 1/27/16.
 */
public abstract class BasePresenter<T> implements Presenter<T> {

    protected final Human human;
    protected final T device;
    protected final Observer observer;
    protected final List<Presentation> presentations = new ArrayList<>();
    protected boolean isCancelled;

    public BasePresenter(Human human, T device, Observer observer) {
        this.human = human;
        this.device = device;
        this.observer = observer;
    }

    public Human getHuman() {
        return human;
    }

    public T getDevice() {
        // TODO override device and remove patches when cancelled.
        return device;
    }

    @Override
    public List<Presentation> getPresentations() {
        return presentations;
    }

    public void addPresentation(Presentation presentation) {
        if (isCancelled) {
            presentation.cancel();
        } else {
            presentations.add(presentation);
        }
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void cancel() {
        if (isCancelled)
            return;
        isCancelled = true;
        final List<Presentation> toCancel = new ArrayList<>(presentations);
        presentations.clear();
        Collections.reverse(toCancel);
        for (Presentation presentation : toCancel) {
            presentation.cancel();
        }
    }

    @Override
    public void onReaction(Reaction reaction) {
        if (isCancelled)
            return;
        observer.onReaction(reaction);
    }

    @Override
    public void onEnd() {
        if (isCancelled)
            return;
        cancel();
        observer.onEnd();
    }

    @Override
    public void onError(Throwable throwable) {
        if (isCancelled)
            return;
        cancel();
        observer.onError(throwable);
    }
}
