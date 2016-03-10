package com.rubyhuntersky.gx.reactions;

/**
 * @author wehjin
 * @since 2/9/16.
 */

public class HeightChangedReaction extends Reaction {
    private final float height;

    public HeightChangedReaction(String source, float height) {
        super(source);
        this.height = height;
    }

    @Override
    public String toString() {
        return "HeightChangedReaction{" +
              "height=" + height +
              "} " + super.toString();
    }
}
