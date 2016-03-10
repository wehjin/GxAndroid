package com.rubyhuntersky.gx.reactions;

import android.support.annotation.Nullable;

/**
 * @author wehjin
 * @since 1/27/16.
 */

public class ItemSelectionReaction<T> extends Reaction {
    private final T item;

    public ItemSelectionReaction(String source, @Nullable T item) {
        super(source);
        this.item = item;
    }

    public T getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "ItemSelectionReaction{" +
              "item=" + item +
              "} " + super.toString();
    }
}
