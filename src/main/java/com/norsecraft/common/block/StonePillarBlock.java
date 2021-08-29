package com.norsecraft.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class StonePillarBlock extends Block {

    public static final EnumProperty<HeightProperty> HEIGHT = EnumProperty.of("pillar_height", HeightProperty.class);

    private final VoxelShape lower = Stream.of(
            Block.createCuboidShape(3, 2, 3, 13, 16, 13),
            Block.createCuboidShape(0, 0, 0, 16, 1, 16),
            Block.createCuboidShape(1, 1, 1, 15, 2, 15)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    private final VoxelShape mid = Block.createCuboidShape(3, 0, 3, 13, 16, 13);
    private final VoxelShape top = Stream.of(
            Block.createCuboidShape(3, 0, 3, 13, 14, 13),
            Block.createCuboidShape(0, 15, 0, 16, 16, 16),
            Block.createCuboidShape(1, 14, 1, 15, 15, 15)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    public StonePillarBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(HEIGHT, HeightProperty.BOTTOM));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (state.get(HEIGHT)) {
            default:
            case BOTTOM:
                return lower;
            case MIDDLE:
                return mid;
            case TOP:
                return top;
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if(!world.isRaining()) {
            world.setBlockState(pos.up(1), state.with(HEIGHT, HeightProperty.MIDDLE));
            world.setBlockState(pos.up(2), state.with(HEIGHT, HeightProperty.TOP));
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HEIGHT);
    }

    @Override
    public void onBreak(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if(!worldIn.isClient) {
            HeightProperty height = state.get(HEIGHT);
            switch (height) {
                case BOTTOM:
                    worldIn.setBlockState(pos.up(1), Blocks.AIR.getDefaultState());
                    worldIn.setBlockState(pos.up(2), Blocks.AIR.getDefaultState());
                    break;
                case MIDDLE:
                    worldIn.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
                    worldIn.setBlockState(pos.down(), Blocks.AIR.getDefaultState());
                    break;
                case TOP:
                    worldIn.setBlockState(pos.down(1), Blocks.AIR.getDefaultState());
                    worldIn.setBlockState(pos.down(2), Blocks.AIR.getDefaultState());
                    break;
            }
        }
        super.onBreak(worldIn, pos, state, player);
    }

    public enum HeightProperty implements StringIdentifiable {

        BOTTOM,
        MIDDLE,
        TOP;

        @Override
        public String asString() {
            return switch (this) {
                case BOTTOM -> "bottom";
                case MIDDLE -> "middle";
                case TOP -> "top";
            };
        }
    }
}
