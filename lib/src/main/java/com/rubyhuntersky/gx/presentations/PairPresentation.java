package com.rubyhuntersky.gx.presentations;

import android.util.Pair;

/**
 * @author wehjin
 * @since 1/30/16.
 */
public class PairPresentation implements Presentation {
    private final Pair<Presentation, Presentation> pair;

    public PairPresentation(Pair<Presentation, Presentation> pair) {
        this.pair = pair;
    }

    @Override
    public float getWidth() {
        return Math.max(pair.first.getWidth(), pair.second.getWidth());
    }

    @Override
    public float getHeight() {
        return Math.max(pair.first.getHeight(), pair.second.getHeight());
    }

    @Override
    public boolean isCancelled() {
        return pair.first.isCancelled();
    }

    @Override
    public void cancel() {
        if (isCancelled()) {
            return;
        }
        pair.first.cancel();
        pair.second.cancel();
    }

}
