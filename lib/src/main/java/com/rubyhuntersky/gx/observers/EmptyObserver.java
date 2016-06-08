package com.rubyhuntersky.gx.observers;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.reactions.Reaction;

/**
 * @author Jeffrey Yu
 * @since 3/10/16.
 */
public class EmptyObserver implements Observer {
    @Override
    public void onReaction(@NonNull Reaction reaction) {
        // Do nothing
    }

    @Override
    public void onEnd() {
        // Do nothing
    }

    @Override
    public void onError(@NonNull Throwable throwable) {
        // Do nothing
    }
}
