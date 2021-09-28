package com.norsecraft.common.block;

import com.norsecraft.common.util.VoxelShapeGroup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BaseDoubleHorizontalBlock extends BaseDirectionalBlock {

    public static final EnumProperty<Side> SIDE = EnumProperty.of("side", Side.class);
    private final VoxelShapeGroup left;
    private final VoxelShapeGroup right;

    public BaseDoubleHorizontalBlock(Settings settings, VoxelShapeGroup left, VoxelShapeGroup right) {
        super(settings, null);
        this.setDefaultState(getStateManager().getDefaultState().with(SIDE, Side.LEFT));
        this.left = left;
        this.right = right;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = getBaseDirection(state).getOpposite();
        Side side = state.get(SIDE);
        switch (side) {
            case RIGHT:
                switch (direction) {
                    default:
                    case NORTH:
                        return right.east;
                    case WEST:
                        return right.north;
                    case EAST:
                        return right.south;
                    case SOUTH:
                        return right.west;
                }
            case LEFT:
                switch (direction) {
                    default:
                    case NORTH:
                        return left.west;
                    case WEST:
                        return left.south;
                    case EAST:
                        return left.north;
                    case SOUTH:
                        return left.east;
                }
            default:
                return right.north;
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(SIDE);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getPlayerFacing();
        BlockPos blockPos = ctx.getBlockPos();
        BlockPos blockPos1 = blockPos.offset(direction);
        return ctx.getWorld().getBlockState(blockPos1).canReplace(ctx) ? this.getDefaultState().with(FACING, direction) : null;
    }


    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient) {
            BlockPos blockPos = pos.offset(state.get(FACING));
            world.setBlockState(blockPos, state.with(SIDE, Side.RIGHT), 3);
            world.updateComparators(pos, Blocks.AIR);
            state.updateNeighbors(world, pos, 3);
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            Direction other = getDirectionToOther(state.get(SIDE), state.get(FACING));
            world.setBlockState(pos.offset(other), Blocks.AIR.getDefaultState());
        }
        super.onBreak(world, pos, state, player);
    }

    private static Direction getDirectionToOther(Side side, Direction direction) {
        return side == Side.LEFT ? direction : direction.getOpposite();
    }

    public static Direction getBaseDirection(BlockState state) {
        Direction direction = state.get(FACING);
        return state.get(SIDE) == Side.RIGHT ? direction.getOpposite() : direction;
    }

    public enum Side implements StringIdentifiable {
        LEFT("left"),
        RIGHT("right");

        private final String name;

        Side(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }

}
