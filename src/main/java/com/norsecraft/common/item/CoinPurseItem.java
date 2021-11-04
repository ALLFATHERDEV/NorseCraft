package com.norsecraft.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class CoinPurseItem extends Item {
  private DefaultedList<ItemStack> inventory;
  public static final int SIZE = 5;

  public CoinPurseItem(Settings settings) {
    super(settings);
    inventory = DefaultedList.ofSize(SIZE, ItemStack.EMPTY);
  }

  public DefaultedList<ItemStack> getInventory() {
    return inventory;
  }

  

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    // TODO: launch inventory screen
    return TypedActionResult.pass(user.getStackInHand(hand));
  }
  
}
