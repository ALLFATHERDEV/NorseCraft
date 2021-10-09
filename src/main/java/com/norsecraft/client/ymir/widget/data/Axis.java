package com.norsecraft.client.ymir.widget.data;

/**
 * Enum that holds the both axis
 */
public enum Axis {

    HORIZONTAL,
    VERTICAL;

    public <T> T choose(T horizontal, T vertical) {
        return this == HORIZONTAL ? horizontal : vertical;
    }

}
