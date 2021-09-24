package com.norsecraft.client.ymir.widget.data;

public enum Axis {

    HORIZONTAL,
    VERTICAL;

    public <T> T choose(T horizontal, T vertical) {
        return this == HORIZONTAL ? horizontal : vertical;
    }

}
