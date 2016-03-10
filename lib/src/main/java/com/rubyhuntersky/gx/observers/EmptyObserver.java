package com.rubyhuntersky.gx.observers;

import com.rubyhuntersky.gx.reactions.Reaction;

/**
 * @author Jeffrey Yu
 * @since 3/10/16.
 */
public class EmptyObserver implements Observer {
    @Override
    public void onReaction(Reaction reaction) {
        // Do nothing
    }

    @Override
    public void onEnd() {
        // Do nothing
    }

    @Override
    public void onError(Throwable throwable) {
        // Do nothing
    }
}
