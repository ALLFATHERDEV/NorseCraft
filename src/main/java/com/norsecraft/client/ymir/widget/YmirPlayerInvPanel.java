package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.screen.BackgroundPainter;
import com.norsecraft.client.ymir.widget.data.NarrationMessages;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

/**
 * A player inventory widget that has a visually separate hotbar.
 */
public class YmirPlayerInvPanel extends YmirPlainPanel {

    private final YmirItemSlot inv;
    private final YmirItemSlot hotbar;

    public YmirPlayerInvPanel(PlayerInventory playerInventory) {
        this(playerInventory, 0);
    }

    public YmirPlayerInvPanel(PlayerInventory inventory, int yOffset) {
        this(inventory, 0, yOffset);
    }

    public YmirPlayerInvPanel(Inventory playerInventory, int xOffset, int yOffset) {
        inv = YmirItemSlot.ofPlayerStorage(playerInventory);
        hotbar = new YmirItemSlot(playerInventory, 0, 9, 1, false) {

            @Override
            public @Nullable Text getNarrationName() {
                return NarrationMessages.HOTBAR;
            }
        };

        this.add(inv, xOffset, yOffset + 10);
        this.add(hotbar, xOffset, yOffset + 68);
    }


    @Override
    public boolean canResize() {
        return false;
    }

    @Override
    public void setBackgroundPainter(BackgroundPainter backgroundPainter) {
        super.setBackgroundPainter(null);
        inv.setBackgroundPainter(backgroundPainter);
        if (hotbar != null)
            hotbar.setBackgroundPainter(backgroundPainter);
    }
}
