package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.widget.data.Insets;

public class YmirPlainPanel extends YmirPanelWithInsets {

    public void add(YmirWidget w, int x, int y) {
        children.add(w);
        w.parent = this;
        w.setLocation(insets.left() + x, insets.top() + y);
        if(w.canResize())
            w.setSize(18, 18);
        expandToFit(w, insets);
    }

    public void add(YmirWidget w, int x, int y, int width, int height) {
        children.add(w);
        w.parent = this;
        w.setLocation(insets.left() + x, insets.top() + y);
        if(w.canResize())
            w.setSize(width, height);

        expandToFit(w, insets);
    }

    @Override
    public YmirPlainPanel setInsets(Insets insets) {
        super.setInsets(insets);
        return this;
    }
}
