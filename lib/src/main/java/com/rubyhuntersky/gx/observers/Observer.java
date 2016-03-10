package com.rubyhuntersky.gx.observers;

import com.rubyhuntersky.gx.reactions.Reaction;

/**
 * @author wehjin
 * @since 1/23/16.
 */

public interface Observer {
    Observer EMPTY = new EmptyObserver();

    void onReaction(Reaction reaction);
    void onEnd();
    void onError(Throwable throwable);
}
