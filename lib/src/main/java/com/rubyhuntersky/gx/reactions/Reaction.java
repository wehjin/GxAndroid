package com.rubyhuntersky.gx.reactions;

/**
 * @author wehjin
 * @since 1/23/16.
 */

abstract public class Reaction {

    private String source;

    public Reaction(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String name) {
        this.source = name;
    }

    @Override
    public String toString() {
        return "Reaction{" +
              "source='" + source + '\'' +
              '}';
    }
}
