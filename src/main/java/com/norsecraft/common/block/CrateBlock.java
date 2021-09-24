package com.norsecraft.common.block;

import com.norsecraft.common.block.entity.CrateBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class CrateBlock extends BlockWithEntity {

    public CrateBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.shape;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CrateBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof CrateBlockEntity) {
            CrateBlockEntity cbe = (CrateBlockEntity) be;
            if (world.isClient)
                cbe.setOpen(true);
            if (!world.isClient) {
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CrateBlockEntity) {
                ItemScatterer.spawn(world, pos, (CrateBlockEntity) blockEntity);
                world.updateComparators(pos, this);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    private final VoxelShape shape = Stream.of(
            Block.createCuboidShape(0, 14, 14, 2, 16, 16),
            Block.createCuboidShape(0, 14, 2, 2, 16, 14),
            Block.createCuboidShape(2, 14.5, 2, 14, 15.5, 14),
            Block.createCuboidShape(2, 0.5, 2, 14, 2, 14),
            Block.createCuboidShape(0, 14, 0, 2, 16, 2),
            Block.createCuboidShape(14, 14, 0, 16, 16, 2),
            Block.createCuboidShape(14, 14, 14, 16, 16, 16),
            Block.createCuboidShape(0, 0, 14, 2, 2, 16),
            Block.createCuboidShape(0, 2, 14, 2, 14, 16),
            Block.createCuboidShape(0, 2, 0, 2, 14, 2),
            Block.createCuboidShape(2, 2, 0.5, 14, 14, 2),
            Block.createCuboidShape(2, 2, 14, 14, 14, 15.5),
            Block.createCuboidShape(0.5, 2, 2, 2, 14, 14),
            Block.createCuboidShape(14, 2, 2, 15.5, 14, 14),
            Block.createCuboidShape(14, 2, 0, 16, 14, 2),
            Block.createCuboidShape(14, 2, 14, 16, 14, 16),
            Block.createCuboidShape(14, 0, 14, 16, 2, 16),
            Block.createCuboidShape(14, 0, 0, 16, 2, 2),
            Block.createCuboidShape(0, 0, 0, 2, 2, 2),
            Block.createCuboidShape(14, 14, 2, 16, 16, 14),
            Block.createCuboidShape(2, 14, 14, 14, 16, 16),
            Block.createCuboidShape(2, 14, 0, 14, 16, 2),
            Block.createCuboidShape(14, 0, 2, 16, 2, 14),
            Block.createCuboidShape(2, 0, 0, 14, 2, 2),
            Block.createCuboidShape(0, 0, 2, 2, 2, 14),
            Block.createCuboidShape(2, 0, 14, 14, 2, 16)
    ).reduce((v1, v2) -> {
        return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);
    }).get();

}
