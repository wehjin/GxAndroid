package com.rubyhuntersky.gx.internal.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.observers.Observer;
import com.rubyhuntersky.gx.presentations.Presentation;
import com.rubyhuntersky.gx.reactions.HeightChangedReaction;
import com.rubyhuntersky.gx.reactions.Reaction;

import java.util.Collections;
import java.util.List;

/**
 * @author wehjin
 * @since 1/31/16.
 */
public class SwitchPresenter<T> implements Presenter<T> {

    public static final String TAG = SwitchPresenter.class.getSimpleName();
    private final Human human;
    private final T device;
    private final Observer observer;
    boolean isCancelled;
    Presentation presentation;
    int presentationCount = 0;

    public SwitchPresenter(Human human, T device, Observer observer) {
        this.human = human;
        this.device = device;
        this.observer = observer;
        presentation = Presentation.EMPTY;
    }

    @Override
    public float getWidth() {
        return presentation.getWidth();
    }

    @Override
    public float getHeight() {
        Log.d(TAG, "getHeight");
        return presentation.getHeight();
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
        presentation.cancel();
    }

    @Override
    public void onReaction(@NonNull Reaction reaction) {
        observer.onReaction(reaction);
    }

    @Override
    public void onEnd() {
        observer.onEnd();
    }

    @Override
    public void onError(@NonNull Throwable throwable) {
        observer.onError(throwable);
    }

    @Override
    public Human getHuman() {
        return human;
    }

    @Override
    public T getDevice() {
        return device;
    }

    @Override
    public List<Presentation> getPresentations() {
        return Collections.singletonList(presentation);
    }

    public void setPresentation(Presentation presentation) {
        if (isCancelled) {
            presentation.cancel();
            return;
        }
        final float previousHeight = this.presentation.getHeight();
        this.presentation.cancel();
        this.presentation = presentation;
        presentationCount++;
        final float height = presentation.getHeight();
        Log.d(TAG, "Height: " + height + ", previous height: " + previousHeight);
        if (height != previousHeight && presentationCount > 1) {
            Log.d(TAG, "Height changed: " + height);
            observer.onReaction(new HeightChangedReaction(TAG, height));
        }
    }

    @Override
    public void addPresentation(Presentation presentation) {
        setPresentation(presentation);
    }
}
