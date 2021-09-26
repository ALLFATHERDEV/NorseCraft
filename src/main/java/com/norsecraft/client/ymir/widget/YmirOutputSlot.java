package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.interpretation.GuiInterpretation;
import com.norsecraft.client.ymir.widget.slot.TradeOutputSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.village.Merchant;
import net.minecraft.village.MerchantInventory;

public class YmirOutputSlot extends YmirWidget {

    private final MerchantInventory merchantInventory;
    private final PlayerEntity player;
    private final Merchant merchant;
    private final int index;

    public YmirOutputSlot(MerchantInventory merchantInventory, PlayerEntity player, Merchant merchant, int index) {
        this.merchantInventory = merchantInventory;
        this.player = player;
        this.merchant = merchant;
        this.index = index;
    }

    @Override
    public int getWidth() {
        return 18;
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    @Override
    public void validate(GuiInterpretation host) {
        super.validate(host);
        TradeOutputSlot outputSlot = new TradeOutputSlot(this.player, this.merchant, this.merchantInventory, index, this.getAbsoluteX() + 19, this.getAbsoluteY() + 19);
        host.addOutputSlot(outputSlot);
    }

}
