package com.norsecraft.common.block.dwarfforge;

import com.norsecraft.common.util.VoxelShapeGroup;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class BaseDwarfForgeDoubleBlock extends BaseDwarfForgeBlock{

    public static final EnumProperty<BlockHalf> HALF = Properties.BLOCK_HALF;

    private final VoxelShapeGroup bottomShapes;
    private final VoxelShapeGroup topShapes;

    public BaseDwarfForgeDoubleBlock(VoxelShapeGroup bottomShapes, VoxelShapeGroup topShapes) {
        super(FabricBlockSettings.copyOf(Blocks.OBSERVER), null);
        this.bottomShapes = bottomShapes;
        this.topShapes = topShapes;
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(HALF, BlockHalf.BOTTOM).with(HORIZONTAL_FACING, ctx.getPlayerFacing());
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if(!world.isClient) {
            world.setBlockState(pos.up(), state.with(HALF, BlockHalf.TOP).with(HORIZONTAL_FACING, placer.getHorizontalFacing()
             == Direction.UP || placer.getHorizontalFacing() == Direction.DOWN ? Direction.NORTH : placer.getHorizontalFacing()));
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if(!world.isClient) {
            switch (state.get(HALF)) {
                case BOTTOM:
                    world.setBlockState(pos.up(), Blocks.AIR.getDefaultState());
                    break;
                case TOP:
                    world.setBlockState(pos.down(), Blocks.AIR.getDefaultState());
                    break;
            }
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HALF);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        BlockHalf half = state.get(HALF);
        return switch (state.get(HORIZONTAL_FACING)) {
            case SOUTH -> half == BlockHalf.BOTTOM ? bottomShapes.south : topShapes.south;
            case WEST -> half == BlockHalf.BOTTOM ? bottomShapes.west : topShapes.west;
            case EAST -> half == BlockHalf.BOTTOM ? bottomShapes.east : topShapes.east;
            default -> half == BlockHalf.BOTTOM ? bottomShapes.north : topShapes.north;
        };
    }
}
