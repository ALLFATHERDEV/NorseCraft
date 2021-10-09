package com.norsecraft.common.block;

import com.norsecraft.common.util.VoxelShapeGroup;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for directional blocks
 * Directional blocks are blocks that have different models for different facings like a chair
 */
public class BaseDirectionalBlock extends HorizontalFacingBlock {

    /**
     * The {@link VoxelShapeGroup} for the block
     */
    protected final VoxelShapeGroup group;

    public BaseDirectionalBlock(Settings settings, VoxelShapeGroup group) {
        super(settings);
        this.group = group;
        this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if(group == null)
            return super.getOutlineShape(state, world, pos, context);
        return switch (state.get(FACING)) {
            case EAST -> group.east;
            case WEST -> group.west;
            case SOUTH -> group.south;
            default -> group.north;
        };
    }
}
