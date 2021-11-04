package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.interpretation.GuiInterpretation;
import com.norsecraft.client.ymir.widget.data.Axis;
import com.norsecraft.client.ymir.widget.data.InputResult;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Similar to the JScrollPane in Swing, this widget represents a scrollable widget.
 */
public class YmirScrollPanel extends YmirClippedPanel {

    private static final int SCROLL_BAR_SIZE = 8;
    private final YmirWidget widget;

    private TriState scrollingHorizontally = TriState.DEFAULT;
    private TriState scrollingVertically = TriState.DEFAULT;

    protected YmirScrollBar horizontalScrollBar = new YmirScrollBar(Axis.HORIZONTAL);
    protected YmirScrollBar verticalScrollBar = new YmirScrollBar(Axis.VERTICAL);

    private int lastHorizontalScroll = -1;
    private int lastVerticalScroll = -1;

    public YmirScrollPanel(YmirWidget widget) {
        this.widget = widget;
        widget.setParent(this);
        horizontalScrollBar.setParent(this);
        verticalScrollBar.setParent(this);
        children.add(widget);
        children.add(verticalScrollBar);
    }

    public TriState isScrollingHorizontally() {
        return this.scrollingHorizontally;
    }

    public YmirScrollPanel setScrollingHorizontally(TriState scrollingHorizontally) {
        if (scrollingHorizontally != this.scrollingHorizontally) {
            this.scrollingHorizontally = scrollingHorizontally;
            layout();
        }
        return this;
    }


    public TriState isScrollingVertically() {
        return this.scrollingHorizontally;
    }

    public YmirScrollPanel setScrollingVertically(TriState scrollingVertically) {
        if (scrollingVertically != this.scrollingVertically) {
            this.scrollingVertically = scrollingVertically;
            layout();
        }
        return this;
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        if (verticalScrollBar.getValue() != lastVerticalScroll || horizontalScrollBar.getValue() != lastHorizontalScroll) {
            layout();
            lastHorizontalScroll = horizontalScrollBar.getValue();
            lastVerticalScroll = verticalScrollBar.getValue();
        }
        super.paint(matrices, x, y, mouseX, mouseY);
    }

    @Override
    public void layout() {
        children.clear();

        boolean horizontal = hasHorizontalScrollbar();
        boolean vertical = hasVerticalScrollbar();

        int offset = (horizontal && vertical) ? SCROLL_BAR_SIZE : 0;
        verticalScrollBar.setSize(SCROLL_BAR_SIZE, this.height - offset);
        verticalScrollBar.setLocation(this.width - verticalScrollBar.getWidth(), 0);
        horizontalScrollBar.setSize(this.width - offset, SCROLL_BAR_SIZE);
        horizontalScrollBar.setLocation(0, this.height - horizontalScrollBar.getHeight());

        if (widget instanceof YmirPanel) ((YmirPanel) widget).layout();
        children.add(widget);
        int x = horizontal ? -horizontalScrollBar.getValue() : 0;
        int y = vertical ? -verticalScrollBar.getValue() : 0;
        widget.setLocation(x, y);

        verticalScrollBar.setWindow(this.height - (horizontal ? SCROLL_BAR_SIZE : 0));
        verticalScrollBar.setMaxValue(widget.getHeight());
        horizontalScrollBar.setWindow(this.width - (vertical ? SCROLL_BAR_SIZE : 0));
        horizontalScrollBar.setMaxValue(widget.getWidth());

        if (vertical)
            children.add(verticalScrollBar);
        if (horizontal)
            children.add(horizontalScrollBar);

    }

    @Override
    public InputResult onMouseScroll(int x, int y, double amount) {
        if (hasVerticalScrollbar())
            return verticalScrollBar.onMouseScroll(0, 0, amount);
        return InputResult.IGNORED;
    }

    @Override
    public void validate(GuiInterpretation host) {
        this.horizontalScrollBar.validate(host);
        this.verticalScrollBar.validate(host);
        super.validate(host);
    }

    private boolean hasHorizontalScrollbar() {
        return (scrollingHorizontally == TriState.DEFAULT)
                ? (widget.width > this.width - SCROLL_BAR_SIZE)
                : scrollingHorizontally.get();
    }

    private boolean hasVerticalScrollbar() {
        return (scrollingVertically == TriState.DEFAULT)
                ? (widget.height > this.height - SCROLL_BAR_SIZE)
                : scrollingVertically.get();
    }
}
