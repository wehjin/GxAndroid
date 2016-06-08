package com.rubyhuntersky.gx.observers;

import android.support.annotation.NonNull;

import com.rubyhuntersky.gx.reactions.Reaction;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public interface Observer {
    Observer EMPTY = new EmptyObserver();

    void onReaction(@NonNull Reaction reaction);
    void onEnd();
    void onError(@NonNull Throwable throwable);
}
