package com.norsecraft.common.gui;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.ymir.interpretation.SyncedGuiInterpretation;
import com.norsecraft.client.ymir.screen.TexturedBackgroundPainter;
import com.norsecraft.client.ymir.widget.YmirGridPanel;
import com.norsecraft.client.ymir.widget.YmirItemSlot;
import com.norsecraft.client.ymir.widget.data.Insets;
import com.norsecraft.client.ymir.widget.data.Texture;
import com.norsecraft.common.block.CrateBlock;
import com.norsecraft.common.block.entity.CrateBlockEntity;
import com.norsecraft.common.registry.NCScreenHandlers;
import com.norsecraft.common.util.BlockEntityUtil;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class CrateGuiInterpretation extends SyncedGuiInterpretation {

    private static final Identifier TEXTURE = NorseCraftMod.ncTex("gui/crate_gui.png");

    private CrateBlockEntity blockEntity;

    public CrateGuiInterpretation(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY, BlockEntityUtil.getBlockEntity(CrateBlockEntity.class, playerInventory.player.world, buf));
    }

    public CrateGuiInterpretation(int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx, CrateBlockEntity entity) {
        super(NCScreenHandlers.crate, syncId, playerInventory,
                getBlockInventory(ctx, 27), null,
                new TexturedBackgroundPainter(new Texture(TEXTURE, 0, 0, 353, 309, 512F, 512F)));
        this.blockEntity = entity;

        YmirGridPanel root = (YmirGridPanel) this.getRootPanel();
        root.setInsets(new Insets(7, 7, 8, 8));
        YmirItemSlot slot = YmirItemSlot.of(blockInventory, 0, 9, 3);
        root.add(slot, 0, 0);
        root.add(createPlayerInventoryPanel(), 0, 3);
        this.getRootPanel().validate(this);
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        if(world.isClient)
            blockEntity.setOpen(false);
    }
}
