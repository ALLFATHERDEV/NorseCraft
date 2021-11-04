package com.norsecraft.client.ymir.widget;

import com.google.common.collect.Lists;
import com.norsecraft.client.ymir.interpretation.GuiInterpretation;
import com.norsecraft.client.ymir.screen.BackgroundPainter;
import com.norsecraft.client.ymir.widget.data.Insets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Panels are widgets that contain other widgets.
 */
public class YmirPanel extends YmirWidget {

    protected final List<YmirWidget> children = new WidgetList(this, Lists.newArrayList());
    protected int anchorX = 0;
    protected int anchorY = 0;

    @Environment(EnvType.CLIENT)
    private BackgroundPainter backgroundPainter = null;

    public void remove(YmirWidget w) {
        children.remove(w);
    }

    public void setAnchor(int anchorX, int anchorY) {
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    public int getAnchorX() {
        return anchorX;
    }

    public int getAnchorY() {
        return anchorY;
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Environment(EnvType.CLIENT)
    public void setBackgroundPainter(BackgroundPainter backgroundPainter) {
        this.backgroundPainter = backgroundPainter;
    }

    @Environment(EnvType.CLIENT)
    public BackgroundPainter getBackgroundPainter() {
        return backgroundPainter;
    }

    /**
     * Resize this panel, so that every widget fits perfect in
     */
    public void layout() {
        for (YmirWidget child : children) {
            if (child instanceof YmirPanel) ((YmirPanel) child).layout();
            expandToFit(child);
        }
    }

    protected void expandToFit(YmirWidget w) {
        expandToFit(w, Insets.NONE);
    }

    /**
     * Expands this panel be at least as large as the widget.
     *
     * @param w      the widget
     * @param insets the layout insets
     */
    protected void expandToFit(YmirWidget w, Insets insets) {
        int pushRight = w.getX() + w.getWidth() + insets.right();
        int pushDown = w.getY() + w.getHeight() + insets.bottom();
        this.setSize(Math.max(this.getWidth(), pushRight), Math.max(this.getHeight(), pushDown));
    }

    /**
     * Finds the most specific child node at this location.
     */
    @Override
    public YmirWidget hit(int x, int y) {
        if (children.isEmpty()) return this;
        for (int i = children.size() - 1; i >= 0; i--) {
            YmirWidget child = children.get(i);
            if (x >= child.getX() &&
                    y >= child.getY() &&
                    x < child.getX() + child.getWidth() &&
                    y < child.getY() + child.getHeight()) {
                return child.hit(x - child.getX(), y - child.getY());
            }
        }
        return this;
    }

    /**
     * Subclasses should call {@code super.validate(c)} to ensure that children are validated.
     * @param host the host GUI description
     */
    @Override
    public void validate(GuiInterpretation host) {
        super.validate(host);
        layout();
        for (YmirWidget child : children)
            child.validate(host);
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        if (backgroundPainter != null) backgroundPainter.paintBackground(matrices, x, y, this);

        for (YmirWidget child : children) {
            child.paint(matrices, x + child.getX(), y + child.getY(), mouseX - child.getX(), mouseY - child.getY());
        }

    }

    @Override
    public void tick() {
        for (YmirWidget child : children) child.tick();
    }

    @Override
    public @Nullable YmirWidget cycleFocus(boolean lookForwards) {
        return cycleFocus(lookForwards, null);
    }

    public @Nullable YmirWidget cycleFocus(boolean lookForwards, @Nullable YmirWidget pivot) {
        if (pivot == null) {
            if (lookForwards) {
                for (YmirWidget child : children) {
                    YmirWidget result = checkFocusCycling(true, child);
                    if (result != null)
                        return result;
                }
            } else if (!children.isEmpty()) {
                for (int i = children.size() - 1; i >= 0; i--) {
                    YmirWidget child = children.get(i);
                    YmirWidget result = checkFocusCycling(false, child);
                    if (result != null)
                        return result;
                }
            }
        } else {
            int currentIndex = children.indexOf(pivot);
            if (currentIndex == -1)
                currentIndex = lookForwards ? 0 : children.size() - 1;
            if (lookForwards) {
                if (currentIndex < children.size() - 1) {
                    for (int i = currentIndex + 1; i < children.size(); i++) {
                        YmirWidget child = children.get(i);
                        YmirWidget result = checkFocusCycling(true, child);
                        if (result != null)
                            return result;
                    }
                }
            } else {
                if (currentIndex > 0) {
                    for (int i = currentIndex - 1; i >= 0; i--) {
                        YmirWidget child = children.get(i);
                        YmirWidget result = checkFocusCycling(false, child);
                        if (result != null)
                            return result;
                    }
                }
            }
        }
        return null;
    }

    private @Nullable YmirWidget checkFocusCycling(boolean lookForwards, YmirWidget child) {
        if (child.canFocus() || child instanceof YmirPanel)
            return child.cycleFocus(lookForwards);
        return null;
    }

    @Override
    public void onShown() {
        for (YmirWidget child : children)
            child.onShown();
    }

    @Override
    public void onHidden() {
        super.onHidden();
        for (YmirWidget child : children)
            child.onHidden();
    }

    @Override
    public void addPainters() {
        for (YmirWidget child : children)
            child.addPainters();
    }

    public final Stream<YmirWidget> streamChildren() {
        return children.stream();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {\n" + children.stream().map(Object::toString).map(x -> x + ",").flatMap(x -> Stream.of(x.split("\n")).filter(y -> !y.isEmpty()).map(y -> "\t" + y)).collect(Collectors.joining("\n")) + "\n}";
    }

    private static final class WidgetList extends AbstractList<YmirWidget> {

        private final YmirPanel owner;
        private final List<YmirWidget> backing;

        private WidgetList(YmirPanel owner, List<YmirWidget> backing) {
            this.owner = owner;
            this.backing = backing;
        }

        @Override
        public YmirWidget get(int index) {
            return backing.get(index);
        }

        private void checkWidget(YmirWidget widget) {
            if (widget == null) {
                throw new NullPointerException("Adding null widget to " + owner);
            }

            int n = 0;
            YmirWidget parent = owner;
            while (parent != null) {
                if (widget == parent) {
                    if (n == 0) {
                        throw new IllegalArgumentException("Adding panel to itself: " + widget);
                    } else {
                        throw new IllegalArgumentException("Adding level " + n + " parent recursively to " + owner + ": " + widget);
                    }
                }

                parent = parent.getParent();
                n++;
            }
        }

        @Override
        public YmirWidget set(int index, YmirWidget element) {
            checkWidget(element);
            return backing.set(index, element);
        }

        @Override
        public void add(int index, YmirWidget element) {
            checkWidget(element);
            backing.add(index, element);
        }

        @Override
        public YmirWidget remove(int index) {
            return backing.remove(index);
        }

        @Override
        public int size() {
            return backing.size();
        }

    }

}
