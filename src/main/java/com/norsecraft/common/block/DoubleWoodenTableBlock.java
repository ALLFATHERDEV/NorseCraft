package com.norsecraft.common.block;

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
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class DoubleWoodenTableBlock extends BaseDirectionalBlock {

    public static final EnumProperty<Side> SIDE = EnumProperty.of("side", Side.class);

    public DoubleWoodenTableBlock(Settings settings) {
        super(settings, null);
        this.setDefaultState(getStateManager().getDefaultState().with(SIDE, Side.LEFT));
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
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = getBaseDirection(state).getOpposite();
        Side side = state.get(SIDE);
        switch (side) {
            case RIGHT:
                switch (direction) {
                    default:
                    case NORTH:
                        return right[1];
                    case WEST:
                        return right[0];
                    case EAST:
                        return right[2];
                    case SOUTH:
                        return right[3];
                }
            case LEFT:
                switch (direction) {
                    default:
                    case NORTH:
                        return left[3];
                    case WEST:
                        return left[2];
                    case EAST:
                        return left[0];
                    case SOUTH:
                        return left[1];
                }
            default:
                return right[0];
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if(!world.isClient) {
            BlockPos blockPos = pos.offset(state.get(FACING));
            world.setBlockState(blockPos, state.with(SIDE, Side.RIGHT), 3);
            world.updateComparators(pos, Blocks.AIR);
            state.updateNeighbors(world, pos, 3);
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if(!world.isClient) {
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

    private VoxelShape[] left = new VoxelShape[]{
            Stream.of(
                    Block.createCuboidShape(0, 14, 0, 16, 16, 16),
                    Block.createCuboidShape(13, 0, 13, 15, 14, 15),
                    Block.createCuboidShape(13, 0, 1, 15, 14, 3),
                    Block.createCuboidShape(13.5, 5, 3, 14.5, 8, 13),
                    Block.createCuboidShape(0, 5, 8, 13.5, 8, 9),
                    Block.createCuboidShape(13.5, 13, 3, 14.5, 14, 13),
                    Block.createCuboidShape(0, 13, 1.5, 13, 14, 2.5),
                    Block.createCuboidShape(0, 13, 13.5, 13, 14, 14.5)
            ).reduce((v1, v2) -> {
                return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);
            }).get(),
            Stream.of(
                    Block.createCuboidShape(0, 14, 0, 16, 16, 16),
                    Block.createCuboidShape(1, 0, 13, 3, 14, 15),
                    Block.createCuboidShape(13, 0, 13, 15, 14, 15),
                    Block.createCuboidShape(3, 5, 13.5, 13, 8, 14.5),
                    Block.createCuboidShape(7, 5, 0, 8, 8, 13.5),
                    Block.createCuboidShape(3, 13, 13.5, 13, 14, 14.5),
                    Block.createCuboidShape(13.5, 13, 0, 14.5, 14, 13),
                    Block.createCuboidShape(1.5, 13, 0, 2.5, 14, 13)
            ).reduce((v1, v2) -> {
                return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);
            }).get(),
            Stream.of(
                    Block.createCuboidShape(0, 14, 0, 16, 16, 16),
                    Block.createCuboidShape(1, 0, 1, 3, 14, 3),
                    Block.createCuboidShape(1, 0, 13, 3, 14, 15),
                    Block.createCuboidShape(1.5, 5, 3, 2.5, 8, 13),
                    Block.createCuboidShape(2.5, 5, 7, 16, 8, 8),
                    Block.createCuboidShape(1.5, 13, 3, 2.5, 14, 13),
                    Block.createCuboidShape(3, 13, 13.5, 16, 14, 14.5),
                    Block.createCuboidShape(3, 13, 1.5, 16, 14, 2.5)
            ).reduce((v1, v2) -> {
                return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);
            }).get(),
            Stream.of(
                    Block.createCuboidShape(0, 14, 0, 16, 16, 16),
                    Block.createCuboidShape(13, 0, 1, 15, 14, 3),
                    Block.createCuboidShape(1, 0, 1, 3, 14, 3),
                    Block.createCuboidShape(3, 5, 1.5, 13, 8, 2.5),
                    Block.createCuboidShape(8, 5, 2.5, 9, 8, 16),
                    Block.createCuboidShape(3, 13, 1.5, 13, 14, 2.5),
                    Block.createCuboidShape(1.5, 13, 3, 2.5, 14, 16),
                    Block.createCuboidShape(13.5, 13, 3, 14.5, 14, 16)
            ).reduce((v1, v2) -> {
                return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);
            }).get()
    };

    private VoxelShape[] right = new VoxelShape[]{
            Stream.of(
                    Block.createCuboidShape(0, 14, 0, 16, 16, 16),
                    Block.createCuboidShape(1, 0, 13, 3, 14, 15),
                    Block.createCuboidShape(1, 0, 1, 3, 14, 3),
                    Block.createCuboidShape(1.5, 5, 3, 2.5, 8, 13),
                    Block.createCuboidShape(2.5, 5, 8, 16, 8, 9),
                    Block.createCuboidShape(1.5, 13, 3, 2.5, 14, 13),
                    Block.createCuboidShape(3, 13, 1.5, 16, 14, 2.5),
                    Block.createCuboidShape(3, 13, 13.5, 16, 14, 14.5)
            ).reduce((v1, v2) -> {
                return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);
            }).get(),
            Stream.of(
                    Block.createCuboidShape(0, 14, 0, 16, 16, 16),
                    Block.createCuboidShape(1, 0, 1, 3, 14, 3),
                    Block.createCuboidShape(13, 0, 1, 15, 14, 3),
                    Block.createCuboidShape(3, 5, 1.5, 13, 8, 2.5),
                    Block.createCuboidShape(7, 5, 2.5, 8, 8, 16),
                    Block.createCuboidShape(3, 13, 1.5, 13, 14, 2.5),
                    Block.createCuboidShape(13.5, 13, 3, 14.5, 14, 16),
                    Block.createCuboidShape(1.5, 13, 3, 2.5, 14, 16)
            ).reduce((v1, v2) -> {
                return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);
            }).get(),
            Stream.of(
                    Block.createCuboidShape(0, 14, 0, 16, 16, 16),
                    Block.createCuboidShape(13, 0, 1, 15, 14, 3),
                    Block.createCuboidShape(13, 0, 13, 15, 14, 15),
                    Block.createCuboidShape(13.5, 5, 3, 14.5, 8, 13),
                    Block.createCuboidShape(0, 5, 7, 13.5, 8, 8),
                    Block.createCuboidShape(13.5, 13, 3, 14.5, 14, 13),
                    Block.createCuboidShape(0, 13, 13.5, 13, 14, 14.5),
                    Block.createCuboidShape(0, 13, 1.5, 13, 14, 2.5)
            ).reduce((v1, v2) -> {
                return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);
            }).get(),
            Stream.of(
                    Block.createCuboidShape(0, 14, 0, 16, 16, 16),
                    Block.createCuboidShape(13, 0, 13, 15, 14, 15),
                    Block.createCuboidShape(1, 0, 13, 3, 14, 15),
                    Block.createCuboidShape(3, 5, 13.5, 13, 8, 14.5),
                    Block.createCuboidShape(8, 5, 0, 9, 8, 13.5),
                    Block.createCuboidShape(3, 13, 13.5, 13, 14, 14.5),
                    Block.createCuboidShape(1.5, 13, 0, 2.5, 14, 13),
                    Block.createCuboidShape(13.5, 13, 0, 14.5, 14, 13)
            ).reduce((v1, v2) -> {
                return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);
            }).get()
    };
    
}
