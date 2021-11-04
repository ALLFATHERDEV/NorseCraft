package com.norsecraft.common.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;

public abstract class InventoryBlockEntity extends LockableContainerBlockEntity {

    protected DefaultedList<ItemStack> inventory;

    public InventoryBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, int inventorySize) {
        super(blockEntityType, blockPos, blockState);
        this.inventory = DefaultedList.ofSize(inventorySize, ItemStack.EMPTY);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, this.inventory);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
        return nbt;
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        Iterator<ItemStack> itemStackIterator = this.inventory.iterator();

        ItemStack itemStack;
        do {
            if(!itemStackIterator.hasNext())
                return true;
            itemStack = itemStackIterator.next();
        } while(itemStack.isEmpty());

        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        if(stack.getCount() > this.getMaxCountPerStack())
            stack.setCount(this.getMaxCountPerStack());
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if(this.world.getBlockEntity(this.pos) != this)
            return false;
        else
            return player.squaredDistanceTo((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

}
