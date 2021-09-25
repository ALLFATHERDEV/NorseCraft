package com.norsecraft.client.gui;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.ymir.interpretation.SyncedGuiInterpretation;
import com.norsecraft.client.ymir.screen.TexturedBackgroundPainter;
import com.norsecraft.client.ymir.widget.YmirButton;
import com.norsecraft.client.ymir.widget.YmirPlainPanel;
import com.norsecraft.client.ymir.widget.data.Texture;
import com.norsecraft.common.entity.dwarf.DwarfBlacksmithEntity;
import com.norsecraft.common.registry.NCScreenHandlers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.Identifier;
import net.minecraft.village.Merchant;
import net.minecraft.world.World;

public class DwarfTradeGuiInterpretation extends SyncedGuiInterpretation {
    private static final Identifier BACKGROUND_TEXTURE = NorseCraftMod.ncTex("gui/npc_gui_trade.png");
    private static final TexturedBackgroundPainter PAINTER = new TexturedBackgroundPainter(new Texture(BACKGROUND_TEXTURE, 0, 0, 720, 332, 1024F, 512F));

    public DwarfTradeGuiInterpretation(int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx, PacketByteBuf buf) {
        this(syncId, playerInventory, ctx, getDwarfBlacksmithFromPos(playerInventory, buf.readInt()));
    }

    public DwarfTradeGuiInterpretation(int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx, Merchant merchant) {
        super(NCScreenHandlers.dwarfTrade, syncId, playerInventory,
                getBlockInventory(ctx), null, null);

        YmirPlainPanel panel = new YmirPlainPanel();
        panel.setBackgroundPainter(PAINTER);
        panel.add(this.createPlayerInventoryPanel(), 79, 73);

        //69 35
        YmirButton dialogButton = new YmirButton(Texture.component(35, 0, 34, 35));
        dialogButton.setHovered(Texture.component(0, 0, 34, 35));
        dialogButton.setOnClick(() -> {
            NorseCraftMod.LOGGER.info("Clicked");
        });
        panel.add(dialogButton, 50, 29, 34, 34);


/*        YmirGridPanel root = (YmirGridPanel) this.getRootPanel();
        root.add(this.createPlayerInventoryPanel(-1, -6), 4, 4);

        YmirButton dialogButton = new YmirButton(new Texture(COMPONENTS, 35, 0, 69, 35, 1024F, 512F));
        dialogButton.setHovered(new Texture(COMPONENTS, 0, 0, 34, 35, 1024F, 512F));
        dialogButton.setOnClick(() -> {
            NorseCraftMod.LOGGER.info("Clicked");
        });
        root.add(dialogButton, 2, 2, 2, 2);
*/
        setRootPanel(panel);
        panel.validate(this);
    }

    private static Merchant getDwarfBlacksmithFromPos(PlayerInventory playerInventory, int entityId) {
        World world = playerInventory.player.world;
        Entity entity = world.getEntityById(entityId);
        if (entity instanceof DwarfBlacksmithEntity) {
            return (DwarfBlacksmithEntity) entity;
        }

        throw new NullPointerException("Could not find dwarf blacksmith");
    }
}
