package com.rubyhuntersky.gx.uis.divs;

import android.util.Log;

import com.rubyhuntersky.gx.Human;
import com.rubyhuntersky.gx.basics.Spot;
import com.rubyhuntersky.gx.internal.surface.Jester;
import com.rubyhuntersky.gx.internal.surface.MoveReaction;
import com.rubyhuntersky.gx.internal.surface.UpReaction;
import com.rubyhuntersky.gx.reactions.TapReaction;

import org.jetbrains.annotations.NotNull;

/**
 * @author Jeffrey Yu
 * @since 6/14/16.
 */
public class TapContact implements Jester.Contact {
    private final Spot downSpot;
    private final Div.Observer observer;
    private final String label;
    private final Spot surfaceOffset;
    private final int maxMoveSquared;

    // TODO Take a lambda instead of a Div.Observer
    public TapContact(Spot downSpot, Div.Observer observer, Human human, String label, Spot surfaceOffset) {
        this.downSpot = downSpot;
        this.observer = observer;
        this.label = label == null ? "enableTap" : label;
        this.surfaceOffset = surfaceOffset;
        final int maxMove = (int) (human.fingerPixels / 4);
        this.maxMoveSquared = maxMove * maxMove;
    }

    @Override
    public void doCancel() {
        Log.d(Div0.TAG, "doCancel");
    }

    @NotNull
    @Override
    public MoveReaction getMoveReaction(@NotNull Spot spot) {
        if (isOutOfBounds(spot)) {
            return MoveReaction.CANCEL;
        } else {
            return MoveReaction.CONTINUE;
        }
    }

    @NotNull
    @Override
    public Jester.Contact doMove(@NotNull Spot spot) {
        return this;
    }

    @NotNull
    @Override
    public UpReaction getUpReaction(@NotNull Spot spot) {
        if (isOutOfBounds(spot)) {
            return UpReaction.CANCEL;
        } else {
            return UpReaction.CONFIRM;
        }
    }

    @Override
    public void doUp(@NotNull Spot spot) {
        long time = System.currentTimeMillis();
        Log.d(Div0.TAG, "doUp " + time);
        observer.onReaction(new TapReaction(label, time, surfaceOffset));
    }

    private boolean isOutOfBounds(@NotNull Spot spot) {
        return spot.distanceSquared(downSpot) > maxMoveSquared;
    }
}
