package com.norsecraft.common.gui;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.ymir.interpretation.SyncedGuiInterpretation;
import com.norsecraft.client.ymir.screen.TexturedBackgroundPainter;
import com.norsecraft.client.ymir.widget.YmirGridPanel;
import com.norsecraft.client.ymir.widget.YmirItemSlot;
import com.norsecraft.client.ymir.widget.data.Insets;
import com.norsecraft.client.ymir.widget.data.Texture;
import com.norsecraft.common.registry.NCScreenHandlers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

public class CrateGuiInterpretation extends SyncedGuiInterpretation {

    private static final Identifier TEXTURE = NorseCraftMod.ncTex("gui/crate_gui.png");

    public CrateGuiInterpretation(int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx) {
        super(NCScreenHandlers.crate, syncId, playerInventory,
                getBlockInventory(ctx, 27), null,
                new TexturedBackgroundPainter(new Texture(TEXTURE, 0, 0, 353, 309, 512F, 512F)));

        YmirGridPanel root = (YmirGridPanel) this.getRootPanel();
        root.setInsets(new Insets(7, 7, 8, 8));
        YmirItemSlot slot = YmirItemSlot.of(blockInventory, 0, 9, 3);
        root.add(slot, 0, 0);
        root.add(createPlayerInventoryPanel(), 0, 3);
        this.getRootPanel().validate(this);
    }

}
