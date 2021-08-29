package com.norsecraft.common.block.dwarfforge;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.block.BaseDirectionalBlock;
import com.norsecraft.common.block.entity.DwarfForgeBlockEntity;
import com.norsecraft.common.util.VoxelShapeGroup;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class BaseDwarfForgeBlock extends BlockWithEntity {

    public static final DirectionProperty HORIZONTAL_FACING = Properties.HORIZONTAL_FACING;

    private final VoxelShapeGroup group;

    public BaseDwarfForgeBlock(Settings settings, VoxelShapeGroup group) {
        super(settings);
        this.group = group;
        this.setDefaultState(this.getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(!world.isClient) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof DwarfForgeBlockEntity) {
                DwarfForgeBlockEntity blockEntity = (DwarfForgeBlockEntity) be;
                if(blockEntity.isFormed()) {
                    NorseCraftMod.LOGGER.info("Multiblock formed");
                } else {
                    NorseCraftMod.LOGGER.info("Multiblock unformed");
                }
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if(!world.isClient) {
            BlockEntity be = world.getBlockEntity(pos);
            if(be instanceof DwarfForgeBlockEntity) {
                DwarfForgeBlockEntity blockEntity = (DwarfForgeBlockEntity) be;
                if(blockEntity.hasMultiblock())
                    blockEntity.unset(pos);
            }
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if(!world.isClient) {
            BlockEntity be = world.getBlockEntity(pos);
            if(be instanceof DwarfForgeBlockEntity) {
                DwarfForgeBlockEntity blockEntity = (DwarfForgeBlockEntity) be;
                blockEntity.scanForMultiblockAndSet(pos, world);
            }
        }

        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DwarfForgeBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if(group == null)
            return this.getOutlineShape(state, world, pos, context);
        switch (state.get(HORIZONTAL_FACING)) {
            case EAST:
                return group.east;
            case WEST:
                return group.west;
            case SOUTH:
                return group.south;
            case NORTH:
            default:
                return group.north;
        }
    }
}
