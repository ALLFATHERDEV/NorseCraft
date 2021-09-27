package com.norsecraft.common.block.variants;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BaseVariantsBlockVar3 extends Block {

    public static final IntProperty VARIANTS_3 = IntProperty.of("block_variants", 1, 3);

    private final VoxelShape shape;

    public BaseVariantsBlockVar3(Settings settings) {
        this(settings, null);
    }

    public BaseVariantsBlockVar3(Settings settings, VoxelShape shape) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(VARIANTS_3, new Random().nextInt(3) + 1));
        this.shape = shape;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return shape != null ? shape : super.getOutlineShape(state, world, pos, context);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(VARIANTS_3);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(VARIANTS_3, new Random().nextInt(3) + 1);
    }
}
