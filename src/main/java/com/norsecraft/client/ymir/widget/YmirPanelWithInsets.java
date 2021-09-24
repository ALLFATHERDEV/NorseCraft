package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.widget.data.Insets;

import java.util.Objects;

public class YmirPanelWithInsets extends YmirPanel {

    protected Insets insets = Insets.NONE;

    public Insets getInsets() {
        return insets;
    }

    public YmirPanelWithInsets setInsets(Insets insets) {
        Insets old = this.insets;
        this.insets = Objects.requireNonNull(insets, "insets");

        setSize(getWidth() - old.left() - old.right(), getHeight() - old.top() - old.bottom());

        for(YmirWidget child : children) {
            child.setLocation(child.getX() - old.left() + insets.left(), child.getY() - old.top() + insets.top());
            expandToFit(child, insets);
        }
        return this;
    }
}
