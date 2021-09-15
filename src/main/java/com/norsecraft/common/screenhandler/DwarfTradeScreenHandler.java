package com.norsecraft.common.screenhandler;

import com.norsecraft.common.entity.dwarf.DwarfBlacksmithEntity;
import com.norsecraft.common.registry.NCScreenHandlers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.TradeOutputSlot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.village.Merchant;
import net.minecraft.village.MerchantInventory;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;

public class DwarfTradeScreenHandler extends ScreenHandler {

    private DwarfBlacksmithEntity merchant;
    private MerchantInventory merchantInventory;

    private static Merchant getDwarfBlacksmithFromPos(PlayerInventory playerInventory, int entityId) {
        World world = playerInventory.player.world;
        Entity entity = world.getEntityById(entityId);
        if (entity instanceof DwarfBlacksmithEntity) {
            return (DwarfBlacksmithEntity) entity;
        }

        throw new NullPointerException("Could not find dwarf blacksmith");
    }

    public DwarfTradeScreenHandler(int id, PlayerInventory playerInventory, PacketByteBuf buffer) {
        this(id, playerInventory, (DwarfBlacksmithEntity) getDwarfBlacksmithFromPos(playerInventory, buffer.readInt()));
    }

    public DwarfBlacksmithEntity getMerchant() {
        return merchant;
    }

    public DwarfTradeScreenHandler(int id, PlayerInventory playerInventory, DwarfBlacksmithEntity merchant) {
        super(NCScreenHandlers.dwarfTrade, id);

        this.merchant = merchant;
        this.merchantInventory = new MerchantInventory(merchant);
        this.addSlot(new Slot(this.merchantInventory, 0, 37, 29));
        this.addSlot(new Slot(this.merchantInventory, 1, 63, 29));
        this.addSlot(new TradeOutputSlot(playerInventory.player, merchant, this.merchantInventory, 2, 120, 29));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 2) {
                if (!this.insertItem(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(itemStack2, itemStack);
            } else if (index != 0 && index != 1) {
                if (index >= 3 && index < 30) {
                    if (!this.insertItem(itemStack2, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !this.insertItem(itemStack2, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        this.merchantInventory.updateOffers();
        super.onContentChanged(inventory);
    }


    public void setCurrentRecipeIndex(int currentRecipeIndex) {
        this.merchantInventory.setOfferIndex(currentRecipeIndex);
    }

    @Override
    public boolean canUse(PlayerEntity playerIn) {
        return true;
    }


    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.merchant.setCurrentCustomer((PlayerEntity) null);
        if (!this.merchant.getMerchantWorld().isClient) {
            if (!player.isAlive() || player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).isDisconnected()) {
                ItemStack itemStack = this.merchantInventory.removeStack(0);
                if (!itemStack.isEmpty()) {
                    player.dropItem(itemStack, false);
                }

                itemStack = this.merchantInventory.removeStack(1);
                if (!itemStack.isEmpty()) {
                    player.dropItem(itemStack, false);
                }
            } else if (player instanceof ServerPlayerEntity) {
                player.getInventory().offerOrDrop(this.merchantInventory.removeStack(0));
                player.getInventory().offerOrDrop(this.merchantInventory.removeStack(1));
            }

        }
    }


    public void switchTo(int recipeIndex) {
        if (this.getOffers().size() > recipeIndex) {
            ItemStack itemStack = this.merchantInventory.getStack(0);
            if (!itemStack.isEmpty()) {
                if (!this.insertItem(itemStack, 3, 39, true)) {
                    return;
                }

                this.merchantInventory.setStack(0, itemStack);
            }

            ItemStack itemStack2 = this.merchantInventory.getStack(1);
            if (!itemStack2.isEmpty()) {
                if (!this.insertItem(itemStack2, 3, 39, true)) {
                    return;
                }

                this.merchantInventory.setStack(1, itemStack2);
            }

            if (this.merchantInventory.getStack(0).isEmpty() && this.merchantInventory.getStack(1).isEmpty()) {
                ItemStack itemStack3 = this.getOffers().get(recipeIndex).getAdjustedFirstBuyItem();
                this.autofill(0, itemStack3);
                ItemStack itemStack4 = this.getOffers().get(recipeIndex).getSecondBuyItem();
                this.autofill(1, itemStack4);
            }

        }
    }

    private void autofill(int slot, ItemStack stack) {
        if (!stack.isEmpty()) {
            for (int i = 3; i < 39; ++i) {
                ItemStack itemStack = ((Slot) this.slots.get(i)).getStack();
                if (!itemStack.isEmpty() && ItemStack.canCombine(stack, itemStack)) {
                    ItemStack itemStack2 = this.merchantInventory.getStack(slot);
                    int j = itemStack2.isEmpty() ? 0 : itemStack2.getCount();
                    int k = Math.min(stack.getMaxCount() - j, itemStack.getCount());
                    ItemStack itemStack3 = itemStack.copy();
                    int l = j + k;
                    itemStack.decrement(k);
                    itemStack3.setCount(l);
                    this.merchantInventory.setStack(slot, itemStack3);
                    if (l >= stack.getMaxCount()) {
                        break;
                    }
                }
            }
        }

    }

    private boolean areItemStacksEqual(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.areEqual(stack1, stack2);
    }


    public TradeOfferList getOffers() {
        return this.merchant.getOffers();
    }
}
