package com.norsecraft.common.screenhandler;

import com.norsecraft.common.block.entity.CrateBlockEntity;
import com.norsecraft.common.registry.NCScreenHandlers;
import com.norsecraft.common.util.BlockEntityUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class CrateBlockScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final CrateBlockEntity cbe;

    public CrateBlockScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new SimpleInventory(27), BlockEntityUtil.getBlockEntity(CrateBlockEntity.class, playerInventory.player.world, buf));
    }

    public CrateBlockScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, CrateBlockEntity cbe) {
        super(NCScreenHandlers.crate, syncId);
        this.cbe = cbe;
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);

        int m;
        int n;
        for (m = 0; m < 3; ++m) {
            for (n = 0; n < 9; ++n) {
                this.addSlot(new Slot(inventory, n + m * 9, 8 + n * 18, 7 + m * 18));
            }
        }

        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 73 + l * 18));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 131));
        }

    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < 27) {
                if (!this.insertItem(itemstack1, 27, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemstack1, 0, 27, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return itemstack;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.cbe.setOpen(false);
    }
}
