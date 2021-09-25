package com.norsecraft.client.ymir.widget;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.ymir.widget.data.Axis;
import com.norsecraft.client.ymir.widget.data.InputResult;
import net.minecraft.client.util.math.MatrixStack;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class YmirListPanel<D, W extends YmirWidget> extends YmirClippedPanel {

    protected List<D> data;
    protected Supplier<W> supplier;
    protected BiConsumer<D, W> configurator;

    protected HashMap<D, W> configured = Maps.newHashMap();
    protected List<W> unconfigured = Lists.newArrayList();
    protected int cellHeight = 20;
    protected boolean fixedHeight = false;
    protected int margin = 2;
    protected YmirScrollBar scrollBar = new YmirScrollBar(Axis.VERTICAL);
    private int lastScroll = -1;

    public YmirListPanel(List<D> data, Supplier<W> supplier, BiConsumer<D, W> configurator) {
        this.data = data;
        this.supplier = supplier;
        this.configurator = configurator;
        scrollBar.setMaxValue(data.size());
        scrollBar.setParent(this);
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        if (scrollBar.getValue() != lastScroll) {
            layout();
            lastScroll = scrollBar.getValue();
        }

        super.paint(matrices, x, y, mouseX, mouseY);
    }

    private W createChild() {
        W child = supplier.get();
        child.setParent(this);
        if (host != null)
            child.setHost(host);
        return child;
    }

    @Override
    public void layout() {
        this.children.clear();
        this.children.add(scrollBar);
        scrollBar.setLocation(this.width - scrollBar.getWidth(), 0);
        scrollBar.setSize(8, this.height);

        if (!fixedHeight) {
            if (unconfigured.isEmpty()) {
                if (configured.isEmpty()) {
                    W exemplar = createChild();
                    unconfigured.add(exemplar);
                    if (!exemplar.canResize()) {
                        cellHeight = exemplar.getHeight();
                        NorseCraftMod.LOGGER.info("DEBUG_1");
                    }
                } else {
                    W exemplar = configured.values().iterator().next();
                    if (!exemplar.canResize()) {
                        cellHeight = exemplar.getHeight();
                        NorseCraftMod.LOGGER.info("DEBUG_2");
                    }
                }
            } else {
                W exemplar = unconfigured.get(0);
                if (!exemplar.canResize()) {
                    cellHeight = exemplar.getHeight();
                    NorseCraftMod.LOGGER.info("DEBUG_3");
                }
            }
        }

        if (cellHeight < 4)
            cellHeight = 4;

        int layoutHeight = this.getHeight() - (margin * 2);
        int cellsHigh = Math.max(layoutHeight / cellHeight, 1);

        scrollBar.setWindow(cellsHigh);
        scrollBar.setMaxValue(data.size());
        int scrollOffset = scrollBar.getValue();

        int presentCells = Math.min(data.size() - scrollOffset, cellsHigh);

        if (presentCells > 0) {
            for (int i = 0; i < presentCells + 1; i++) {
                int index = i + scrollOffset;
                if (index >= data.size())
                    break;
                if (index < 0)
                    continue;
                D d = data.get(index);
                W w = configured.get(d);
                if (w == null) {
                    if (unconfigured.isEmpty())
                        w = createChild();
                    else
                        w = unconfigured.remove(0);

                    configurator.accept(d, w);
                    configured.put(d, w);
                }

                if (w.canResize()) {
                    w.setSize(this.width - (margin * 2) - scrollBar.getWidth(), cellHeight);
                }


                w.x = margin;
                w.y = margin + ((cellHeight + margin) * i);

                this.children.add(w);
            }
        }
    }

    public YmirListPanel<D, W> setListItemHeight(int height) {
        cellHeight = height;
        fixedHeight = true;
        return this;
    }

    @Override
    public InputResult onMouseScroll(int x, int y, double amount) {
        return this.scrollBar.onMouseScroll(0, 0, amount);
    }
}
