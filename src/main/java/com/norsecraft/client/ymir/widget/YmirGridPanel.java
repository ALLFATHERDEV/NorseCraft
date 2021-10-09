package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.widget.data.Insets;

/**
 * A panel that positions children in a grid.
 */
public class YmirGridPanel extends YmirPanelWithInsets {

    /**
     * The grid size in pixels.
     * Defaults to 18, which is the size of one item slot.
     */
    protected int grid = 18;

    public YmirGridPanel() {

    }

    public YmirGridPanel(int gridSize) {
        this.grid = gridSize;
    }

    public void add(YmirWidget w, int x, int y) {
        children.add(w);
        w.parent = this;
        w.setLocation(x * grid + insets.left(), y * grid + insets.top());
        if (w.canResize())
            w.setSize(grid, grid);
        expandToFit(w, insets);
    }

    public void add(YmirWidget w, int x, int y, int width, int height) {
        children.add(w);
        w.parent = this;
        w.setLocation(x * grid + insets.left(), y * grid + insets.top());
        if (w.canResize())
            w.setSize(width * grid, height * grid);
        expandToFit(w, insets);
    }

    @Override
    public YmirGridPanel setInsets(Insets insets) {
        super.setInsets(insets);
        return this;
    }
}
