package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.widget.data.Axis;
import com.norsecraft.client.ymir.widget.data.HorizontalAlignment;
import com.norsecraft.client.ymir.widget.data.Insets;
import com.norsecraft.client.ymir.widget.data.VerticalAlignment;

import java.util.Objects;

/**
 * Similar to the BoxLayout in Swing, this widget represents a list of widgets along an axis.
 */
public class YmirBox extends YmirPanelWithInsets {
    /**
     * The spacing between widgets.
     */
    protected int spacing = 4;

    /**
     * The axis that the widgets are laid out on.
     */
    protected Axis axis;

    /**
     * The horizontal alignment for this box's children.
     */
    protected HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;

    /**
     * The vertical alignment for this box's children.
     */
    protected VerticalAlignment verticalAlignment = VerticalAlignment.TOP;

    public YmirBox(Axis axis) {
        this.axis = Objects.requireNonNull(axis, "axis");
    }

    public void add(YmirWidget widget, int width, int height) {
        widget.setParent(this);
        children.add(widget);
        if (canResize())
            widget.setSize(width, height);
    }

    public void add(YmirWidget widget) {
        this.add(widget, 18, 18);
    }

    @Override
    public void layout() {
        int dimension = axis.choose(insets.left(), insets.top());

        if (axis == Axis.HORIZONTAL && horizontalAlignment != HorizontalAlignment.LEFT) {
            int widgetWidth = spacing * (children.size() - 1);
            for (YmirWidget child : children)
                widgetWidth += child.getWidth();

            if (horizontalAlignment == HorizontalAlignment.CENTER)
                dimension = (getWidth() - widgetWidth) / 2;
            else
                dimension = getWidth() - widgetWidth;
        } else if (verticalAlignment != VerticalAlignment.TOP) {
            int widgetHeight = spacing * (children.size() - 1);
            for (YmirWidget child : children)
                widgetHeight += child.getHeight();

            if (verticalAlignment == VerticalAlignment.CENTER)
                dimension = (getHeight() - widgetHeight) / 2;
            else
                dimension = getHeight() - widgetHeight;

        }

        for (int i = 0; i < children.size(); i++) {
            YmirWidget child = children.get(i);

            if (axis == Axis.HORIZONTAL) {
                int y = switch (verticalAlignment) {
                    case TOP -> insets.top();
                    case CENTER -> insets.top() + (getHeight() - insets.top() - insets.bottom() - child.getHeight()) / 2;
                    case BOTTOM -> getHeight() - insets.bottom() - child.getHeight();
                };

                child.setLocation(dimension, x);
            } else {
                int x = switch (horizontalAlignment) {
                    case LEFT -> insets.left();
                    case CENTER -> insets.left() + (getWidth() - insets.left() - insets.right() - child.getWidth()) / 2;
                    case RIGHT -> getWidth() - insets.right() - child.getWidth();
                };
                child.setLocation(x, dimension);
            }

            if (child instanceof YmirPanel) ((YmirPanel) child).layout();

            expandToFit(child, insets);

            if (i != children.size() - 1)
                dimension += spacing;

            dimension += axis.choose(child.getWidth(), child.getHeight());
        }
    }

    public int getSpacing() {
        return spacing;
    }

    public YmirBox setSpacing(int spacing) {
        this.spacing = spacing;
        return this;
    }

    public Axis getAxis() {
        return axis;
    }

    public YmirBox setAxis(Axis axis) {
        this.axis = Objects.requireNonNull(axis, "axis");
        return this;
    }

    public HorizontalAlignment getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public YmirBox setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = Objects.requireNonNull(horizontalAlignment, "alignment");
        return this;
    }


    public VerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }

    public YmirBox setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = Objects.requireNonNull(verticalAlignment, "alignment");
        return this;
    }

    @Override
    public YmirBox setInsets(Insets insets) {
        super.setInsets(insets);
        return this;
    }
}
