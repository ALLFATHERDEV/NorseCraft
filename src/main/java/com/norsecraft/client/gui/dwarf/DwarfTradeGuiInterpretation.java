package com.norsecraft.client.gui.dwarf;

import com.google.common.collect.Lists;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.ymir.interpretation.MerchantGuiInterpretation;
import com.norsecraft.client.ymir.screen.TexturedBackgroundPainter;
import com.norsecraft.client.ymir.screen.YmirClientScreen;
import com.norsecraft.client.ymir.widget.*;
import com.norsecraft.client.ymir.widget.data.Texture;
import com.norsecraft.common.entity.NorseCraftMerchant;
import com.norsecraft.common.entity.dwarf.DwarfBlacksmithEntity;
import com.norsecraft.common.network.c2s.SelectMerchantRecipeIndexPacketC2S;
import com.norsecraft.common.registry.NCScreenHandlers;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.village.MerchantInventory;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;

import java.util.List;

public class DwarfTradeGuiInterpretation extends MerchantGuiInterpretation {

    private static final Identifier BACKGROUND_TEXTURE = NorseCraftMod.ncTex("gui/npc_gui_trade.png");
    private static final TexturedBackgroundPainter PAINTER = new TexturedBackgroundPainter(new Texture(BACKGROUND_TEXTURE, 0, 0, 720, 332, 1024F, 512F));

    private final NorseCraftMerchant<DwarfBlacksmithEntity> merchant;

    public DwarfTradeGuiInterpretation(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, getDwarfBlacksmithFromPos(playerInventory, buf.readInt()));
    }

    public DwarfTradeGuiInterpretation(int syncId, PlayerInventory playerInventory, NorseCraftMerchant<DwarfBlacksmithEntity> merchant) {
        super(NCScreenHandlers.dwarfTrade, syncId, playerInventory,
                getEntityInventory(merchant.getMerchantEntity()), null, null);

        this.merchant = merchant;
        YmirPlainPanel panel = new YmirPlainPanel();
        panel.setBackgroundPainter(PAINTER);
        panel.setAnchor(144, 0);
        panel.add(this.createPlayerInventoryPanel(), 79, 73);

        YmirLabel label = new YmirLabel(merchant.getMerchantEntity().getCustomName(), 0xFFFFFF);
        panel.add(label, 8, 11, 4, 30);

        YmirButton dialogButton = new YmirButton(Texture.component(35, 0, 34, 36));
        dialogButton.setHovered(Texture.component(0, 0, 34, 36));
        dialogButton.setOnClick(() -> {
            MinecraftClient.getInstance().setScreen(new YmirClientScreen(new DwarfDialogGuiInterpretation(merchant.getMerchantEntity())));
        });
        panel.add(dialogButton, 50, 29, 34, 36);

        YmirButton tradingButton = new YmirButton(Texture.component(0, 35, 34, 36));
        tradingButton.setOnClick(() -> {
            NorseCraftMod.LOGGER.info("Nothing");
        });
        panel.add(tradingButton, 50, 48, 34, 36);

        YmirButton questButton = new YmirButton(Texture.component(35, 72, 34, 36));
        questButton.setHovered(Texture.component(0, 72, 34, 36));
        questButton.setOnClick(() -> {
            NorseCraftMod.LOGGER.info("Quest");
        });
        panel.add(questButton, 50, 68, 34, 36);

        Texture reputationTexture;
        int reputation = merchant.getMerchantEntity().getPlayerReputation(playerInventory.player);
        if (reputation >= 50) {
            //Good
            reputationTexture = Texture.component(104, 1, 24, 24);
        } else if (reputation >= -50) {
            //Neutral
            reputationTexture = Texture.component(104, 27, 24, 24);
        } else {
            //Bad
            reputationTexture = Texture.component(104, 53, 24, 24);
        }

        YmirButton reputationSymbol = new YmirButton(reputationTexture);
        panel.add(reputationSymbol, 53, 8, 24, 24);

        List<TradeOffer> offers = Lists.newArrayList();
        offers.addAll(merchant.getOffers());

        YmirListPanel<TradeOffer, TradeWidget> listPanel = new YmirListPanel<>(offers, TradeWidget::new,
                (offer, button) -> {
                    button.setOffer(offer, merchant.getOffers());
                });
        listPanel.setScrollBarXOffset(74);
        listPanel.setListItemHeight(25);

        panel.add(listPanel, 255, 6, 174, 137);

        //Entity inventory
        YmirItemSlot firstBuySlot = YmirItemSlot.of(blockInventory, 0);
        YmirItemSlot secondBuySlot = YmirItemSlot.of(blockInventory, 1);
        YmirOutputSlot sellSlot = new YmirOutputSlot((MerchantInventory) blockInventory, playerInventory.player, merchant.getMerchantEntity(), 2);

        panel.add(firstBuySlot, 108, 27);
        panel.add(secondBuySlot, 134, 27);
        panel.add(sellSlot, 173, 9);

        setRootPanel(panel);
        panel.validate(this);
    }

    private static NorseCraftMerchant<DwarfBlacksmithEntity> getDwarfBlacksmithFromPos(PlayerInventory playerInventory, int entityId) {
        World world = playerInventory.player.world;
        Entity entity = world.getEntityById(entityId);
        if (entity instanceof DwarfBlacksmithEntity) {
            return (DwarfBlacksmithEntity) entity;
        }

        throw new NullPointerException("Could not find dwarf blacksmith");
    }

    @Override
    public TradeOfferList getOffers() {
        return this.merchant.getOffers();
    }

    @Override
    public NorseCraftMerchant<DwarfBlacksmithEntity> getMerchant() {
        return merchant;
    }

    private class TradeWidget extends YmirPlainPanel {

        private TradeOffer offer;
        private int offerIndex = -1;

        public TradeWidget() {
            YmirButton button = new YmirButton(Texture.component(269, 54, 174, 56));
            button.setOnClick(() -> {
                if (offer == null || offerIndex == -1) {
                    NorseCraftMod.LOGGER.warn("Tried to execute a offer, offer is null");
                    return;
                }
                this.syncRecipeIndex();

            });
            this.add(button, 0, 0, 174, 56);
            this.setSize(174, 56);
        }

        public void setOffer(TradeOffer offer, TradeOfferList offers) {
            this.offer = offer;
            this.offerIndex = offers.indexOf(offer);
            YmirItem firstBuy = new YmirItem(offer.getAdjustedFirstBuyItem());
            this.add(firstBuy, 6, 6);
            if (offer.getSecondBuyItem() != null && !offer.getSecondBuyItem().isEmpty()) {
                YmirItem secondBuy = new YmirItem(offer.getSecondBuyItem());
                this.add(secondBuy, 31, 6);
            }
            YmirItem sell = new YmirItem(offer.getSellItem());
            this.add(sell, 65, 6);

        }

        private void syncRecipeIndex() {
            DwarfTradeGuiInterpretation.this.setOfferIndex(this.offerIndex);
            DwarfTradeGuiInterpretation.this.switchTo(this.offerIndex);
            ClientPlayNetworking.send(SelectMerchantRecipeIndexPacketC2S.SELECT_MERCHANT_RECIPE_INDEX, new SelectMerchantRecipeIndexPacketC2S(this.offerIndex).write());
        }

    }


}
