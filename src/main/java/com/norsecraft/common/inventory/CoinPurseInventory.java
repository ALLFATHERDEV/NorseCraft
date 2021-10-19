package com.norsecraft.common.inventory;

import com.norsecraft.common.item.CoinPurseItem;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class CoinPurseInventory implements Inventory {
  public static final int PURSE_SIZE = 5;
  public static final int NOT_FOUND = -1;
  public final DefaultedList<ItemStack> purse;
  private int changeCount;

  public CoinPurseInventory() {
    this.purse = DefaultedList.ofSize(PURSE_SIZE, ItemStack.EMPTY);
    changeCount = 0;
  }

  public boolean canStackAddMore(ItemStack existingStack, ItemStack stack) {
    return !existingStack.isEmpty() && ItemStack.canCombine(existingStack, stack) && existingStack.isStackable() && existingStack.getCount() < existingStack.getMaxCount() && existingStack.getCount() < getMaxCountPerStack();
  }

  public int getEmptySlot() {
    for (int i = 0; i < purse.size(); ++i) {
      if (((ItemStack) purse.get(i)).isEmpty()) {
        return i;
      }
    }

    return NOT_FOUND;
  }

  @Override
  public void clear() {
    purse.clear();
  }

  @Override
  public int size() {
    return purse.size();
  }

  @Override
  public boolean isEmpty() {
    var empty = true;
    for(var stack : purse) {
      if (!stack.isEmpty()) {
        empty = false;
        break;
      }
    }
    return empty;
  }

  @Override
  public ItemStack getStack(int slot) {
    return inRange(slot) ? purse.get(slot) : ItemStack.EMPTY;
  }

  @Override
  public ItemStack removeStack(int slot, int amount) {
    return inRange(slot) ? purse.get(slot).split(amount) : ItemStack.EMPTY;
  }

  @Override
  public ItemStack removeStack(int slot) {
    if(!inRange(slot)) {
      return ItemStack.EMPTY;
    }
    var stack = purse.get(slot);
    purse.set(slot, ItemStack.EMPTY);
    return stack;
  }

  @Override
  public void setStack(int slot, ItemStack stack) {
    if (inRange(slot)) {
      purse.set(slot, stack);
    }
  }

  @Override
  public void markDirty() {
    ++changeCount;
  }
  public int changeCount() {
    return changeCount;
  }

  @Override
  public boolean canPlayerUse(PlayerEntity player) {

    if(player.getMainHandStack().getItem() instanceof CoinPurseItem purse && purse.getInventory().equals(this)) {
      return true;
    } 
    if (player.getOffHandStack().getItem() instanceof CoinPurseItem purse && purse.getInventory().equals(this)) {
      return true;
    }
    return false;
  }
  
  private boolean inRange(int slot) {
    return slot >= 0 && slot < purse.size();
  }
}
