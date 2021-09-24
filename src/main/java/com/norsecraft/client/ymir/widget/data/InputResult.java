package com.norsecraft.client.ymir.widget.data;

public enum InputResult {

    PROCESSED,
    IGNORED;

    public static InputResult of(boolean processed) {
        return processed ? PROCESSED : IGNORED;
    }

}
