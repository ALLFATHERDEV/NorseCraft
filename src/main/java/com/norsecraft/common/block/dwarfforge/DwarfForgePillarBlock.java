package com.norsecraft.common.block.dwarfforge;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.block.entity.DwarfForgeBlockEntity;
import com.norsecraft.common.util.VoxelShapeGroups;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class DwarfForgePillarBlock extends BaseDwarfForgeDoubleBlock {

    public DwarfForgePillarBlock() {
        super(VoxelShapeGroups.DwarfForge.DWARF_FORGE_PILLAR_BOTTOM_SHAPE, VoxelShapeGroups.DwarfForge.DWARF_FORGE_PILLAR_TOP_SHAPE);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient) {
            BlockHalf half = state.get(HALF);
            Direction facing = state.get(HORIZONTAL_FACING);
            BlockEntity be = world.getBlockEntity(pos);
            if(be instanceof DwarfForgeBlockEntity) {
                DwarfForgeBlockEntity entity = (DwarfForgeBlockEntity) be;
                if(!entity.hasMultiblock()) {
                    NorseCraftMod.getMultiblockManager().getMultiblock(DwarfForgeMultiblock.ID).set(half == BlockHalf.TOP ? pos.down() : pos, facing, world);
                }
            }
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }
}
