package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.screen.BackgroundPainter;
import net.minecraft.inventory.Inventory;

public class YmirInventoryPanel extends YmirPlainPanel {

    private final YmirItemSlot inv;

    public YmirInventoryPanel(Inventory inventory, int slotsWide, int slotsHigh) {
        this.inv = YmirItemSlot.of(inventory, 0, slotsWide, slotsHigh);
        this.add(inv, 0, 0);
    }

    @Override
    public boolean canResize() {
        return false;
    }

    @Override
    public void setBackgroundPainter(BackgroundPainter backgroundPainter) {
        super.setBackgroundPainter(null);
        inv.setBackgroundPainter(backgroundPainter);
    }

}
