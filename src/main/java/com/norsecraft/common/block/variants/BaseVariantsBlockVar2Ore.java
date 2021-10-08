package com.norsecraft.common.block.variants;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BaseVariantsBlockVar2Ore extends OreBlock {

    public static final IntProperty VARIANTS_2 = IntProperty.of("block_variants", 1, 2);

    private final VoxelShape shape;

    public BaseVariantsBlockVar2Ore(Settings settings) {
        this(settings, null);
    }

    public BaseVariantsBlockVar2Ore(Settings settings, VoxelShape shape) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(VARIANTS_2, new Random().nextInt(2) + 1));
        this.shape = shape;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return shape != null ? shape : super.getOutlineShape(state, world, pos, context);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(VARIANTS_2);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(VARIANTS_2, new Random().nextInt(2) + 1);
    }
    
}
