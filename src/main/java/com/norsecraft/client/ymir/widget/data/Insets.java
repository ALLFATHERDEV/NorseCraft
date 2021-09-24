package com.norsecraft.client.ymir.widget.data;

public record Insets(int top, int left, int bottom, int right) {

    public static final Insets NONE = new Insets(0);
    public static final Insets ROOT_PANEL = new Insets(7);

    public Insets {
        if (top < 0) throw new IllegalArgumentException("top cannot be negative, found " + top);
        if (left < 0) throw new IllegalArgumentException("left cannot be negative, found " + left);
        if (bottom < 0) throw new IllegalArgumentException("bottom cannot be negative, found " + bottom);
        if (right < 0) throw new IllegalArgumentException("right cannot be negative, found " + right);
    }

    public Insets(int vertical, int horizontal) {
        this(vertical, horizontal, vertical, horizontal);
    }

    public Insets(int size) {
        this(size, size, size, size);
    }

}
