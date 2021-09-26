package com.norsecraft.client.ymir.interpretation;

import com.google.common.collect.Lists;
import com.norsecraft.client.ymir.screen.BackgroundPainter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.village.Merchant;
import net.minecraft.village.MerchantInventory;
import net.minecraft.village.TradeOfferList;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class MerchantGuiInterpretation extends SyncedGuiInterpretation {


    public MerchantGuiInterpretation(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, @Nullable Inventory blockInventory, @Nullable PropertyDelegate delegate, BackgroundPainter painter) {
        super(type, syncId, playerInventory, blockInventory, delegate, painter);
    }

    public abstract TradeOfferList getOffers();

    public abstract Merchant getMerchant();

    @Override
    public void onContentChanged(Inventory inventory) {
        ((MerchantInventory) this.blockInventory).updateOffers();
        super.onContentChanged(inventory);
    }

    public void setOfferIndex(int index) {
        ((MerchantInventory) this.blockInventory).setOfferIndex(index);
    }

    @Override
    public void close(PlayerEntity player) {
        MerchantInventory merchantInventory = (MerchantInventory) blockInventory;
        super.close(player);
        this.getMerchant().setCurrentCustomer(null);
        if(!this.getMerchant().getMerchantWorld().isClient) {
            if(!player.isAlive() || player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).isDisconnected()) {
                ItemStack itemStack = merchantInventory.removeStack(0);
                if(!itemStack.isEmpty())
                    player.dropItem(itemStack, false);

                itemStack = merchantInventory.removeStack(1);
                if(!itemStack.isEmpty())
                    player.dropItem(itemStack, false);
            } else if(player instanceof ServerPlayerEntity) {
                player.getInventory().offerOrDrop(merchantInventory.removeStack(0));
                player.getInventory().offerOrDrop(merchantInventory.removeStack(1));
            }
        }
    }

    public void switchTo(int recipeIndex) {
        MerchantInventory merchantInventory = (MerchantInventory) this.blockInventory;
        if (this.getOffers().size() > recipeIndex) {
            ItemStack itemStack = merchantInventory.getStack(0);
            if (!itemStack.isEmpty()) {
                if (!this._insertItem(itemStack, merchantInventory, true, playerInventory.player))
                    return;

                merchantInventory.setStack(0, itemStack);
            }

            ItemStack itemStack2 = merchantInventory.getStack(1);
            if (!itemStack.isEmpty()) {
                if (!this._insertItem(itemStack2, merchantInventory, true, playerInventory.player))
                    return;

                merchantInventory.setStack(1, itemStack2);
            }

            if (merchantInventory.getStack(0).isEmpty() && merchantInventory.getStack(1).isEmpty()) {
                ItemStack itemstack3 = this.getOffers().get(recipeIndex).getAdjustedFirstBuyItem();
                this.autofill(0, itemstack3);
                ItemStack itemstack4 = this.getOffers().get(recipeIndex).getSecondBuyItem();
                this.autofill(1, itemstack4);
            }

        }
    }

    private void autofill(int slotIndex, ItemStack stack) {
        if (!stack.isEmpty()) {
            MerchantInventory merchantInventory = (MerchantInventory) this.blockInventory;
            List<Slot> inventorySlots = Lists.newArrayList();
            for (Slot s : slots)
                if (s.inventory == playerInventory)
                    inventorySlots.add(s);

            if (inventorySlots.isEmpty())
                return;

            for (Slot slot : inventorySlots) {
                ItemStack itemstack = slot.getStack();
                if (!itemstack.isEmpty() && ItemStack.canCombine(stack, itemstack)) {
                    ItemStack itemstack2 = merchantInventory.getStack(slotIndex);
                    int j = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
                    int k = Math.min(stack.getMaxCount() - j, itemstack.getCount());
                    ItemStack itemstack3 = itemstack.copy();
                    int l = j + k;
                    itemstack.decrement(k);
                    itemstack3.setCount(l);
                    merchantInventory.setStack(slotIndex, itemstack3);
                    if (l >= stack.getMaxCount())
                        break;
                }
            }

        }
    }

}
