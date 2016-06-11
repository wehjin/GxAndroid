package com.rubyhuntersky.gx.internal.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.devices.poles.Pole;
import com.rubyhuntersky.gx.reactions.HeightChangedReaction;
import com.rubyhuntersky.gx.reactions.Reaction;
import com.rubyhuntersky.gx.uis.divs.Div;

import org.jetbrains.annotations.NotNull;

/**
 * @author wehjin
 * @since 1/31/16.
 */
public class SwitchDivPresenter implements Div.Presenter {

    public static final String TAG = SwitchDivPresenter.class.getSimpleName();
    private final Human human;
    private final Pole device;
    private final Div.Observer observer;
    boolean isCancelled;
    Div.Presentation presentation;
    int heightCount = 0;
    float height = 0;

    public SwitchDivPresenter(Human human, Pole device, Div.Observer observer) {
        this.human = human;
        this.device = device;
        this.observer = observer;
        presentation = Div.CancelledPresentation.INSTANCE;
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
    public void onError(@NonNull Throwable throwable) {
        observer.onError(throwable);
    }

    @NonNull
    @Override
    public Human getHuman() {
        return human;
    }

    @NotNull
    @Override
    public Pole getPole() {
        return device;
    }

    public Pole getDevice() {
        return getPole();
    }

    @Override
    public void onHeight(float height) {
        final float previousHeight = this.height;
        this.height = height;
        Log.d(TAG, "Height: " + height + ", previous height: " + previousHeight);
        heightCount++;
        observer.onHeight(height);
        if (height != previousHeight && heightCount > 1) {
            Log.d(TAG, "Height changed: " + height);
            observer.onReaction(new HeightChangedReaction(TAG, height));
        }
    }

    public void setPresentation(Div.Presentation presentation) {
        if (isCancelled) {
            presentation.cancel();
            return;
        }
        this.presentation.cancel();
        this.presentation = presentation;
    }

    @Override
    public void addPresentation(@NonNull Div.Presentation presentation) {
        setPresentation(presentation);
    }
}
