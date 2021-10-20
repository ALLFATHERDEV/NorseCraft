package com.norsecraft.common.item;

import com.norsecraft.common.block.CampfireBlock;
import com.norsecraft.common.block.entity.CampfireBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

import java.util.Objects;

public class CampfireStandItem extends Item {

    public CampfireStandItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world.isClient) {
            BlockState block = world.getBlockState(context.getBlockPos());
            if (block.getBlock() instanceof CampfireBlock) {
                BlockEntity blockEntity = world.getBlockEntity(context.getBlockPos());
                if (blockEntity instanceof CampfireBlockEntity cbe) {
                    //cbe.setActive(true);
                    Objects.requireNonNull(context.getPlayer()).getInventory().removeStack((context.getPlayer().getInventory().indexOf(context.getStack())),
                            1);
                }
            }
        }
        return ActionResult.SUCCESS;
    }

}
