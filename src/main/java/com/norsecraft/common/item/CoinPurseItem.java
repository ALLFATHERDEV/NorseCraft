package com.norsecraft.common.item;

import com.norsecraft.common.inventory.CoinPurseInventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CoinPurseItem extends Item {
  private CoinPurseInventory inventory = new CoinPurseInventory();

  public CoinPurseItem(Settings settings) {
    super(settings);
  }

  public CoinPurseInventory getInventory() {
    return inventory;
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    // TODO: launch inventory screen
    return TypedActionResult.pass(user.getStackInHand(hand));
  }
  
}
