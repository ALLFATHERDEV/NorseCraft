package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.interpretation.GuiInterpretation;
import com.norsecraft.client.ymir.screen.BackgroundPainter;
import com.norsecraft.client.ymir.widget.data.NarrationMessages;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class YmirPlayerInvPanel extends YmirPlainPanel {

    private final YmirItemSlot inv;
    private final YmirItemSlot hotbar;

    public YmirPlayerInvPanel(Inventory playerInventory) {
        inv = YmirItemSlot.ofPlayerStorage(playerInventory);
        hotbar = new YmirItemSlot(playerInventory, 0, 9, 1, false) {

            @Override
            public @Nullable Text getNarrationName() {
                return NarrationMessages.HOTBAR;
            }
        };

        this.add(inv, 0, 0);
        this.add(hotbar, 0, 58);

    }

    @Override
    public boolean canResize() {
        return false;
    }

    public static YmirLabel createInventoryLabel(Inventory playerInventory) {
        YmirLabel label = new YmirLabel(playerInventory instanceof PlayerInventory inventory ? inventory.getDisplayName() : new LiteralText(""));
        label.setSize(9 * 18, 11);
        return label;
    }

    @Override
    public void setBackgroundPainter(BackgroundPainter backgroundPainter) {
        super.setBackgroundPainter(null);
        inv.setBackgroundPainter(backgroundPainter);
        if (hotbar != null)
            hotbar.setBackgroundPainter(backgroundPainter);
    }
}
