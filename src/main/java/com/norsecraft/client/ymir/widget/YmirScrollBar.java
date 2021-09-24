package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.widget.data.Axis;

public class YmirScrollBar extends YmirWidget {

    private static final int SCROLLING_SPEED = 4;

    protected Axis axis = Axis.HORIZONTAL;
    protected int value;
    protected int maxValue = 100;
    protected int window = 16;

    protected int anchor = -1;
    protected int anchorValue = -1;
    protected boolean sliding = false;

    public YmirScrollBar() {

    }

    public YmirScrollBar(Axis axis) {
        this.axis = axis;
    }

}
