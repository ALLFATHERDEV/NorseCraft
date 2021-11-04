package com.norsecraft.common.block;

import com.norsecraft.common.block.entity.CampfireBlockEntity;
import com.norsecraft.common.network.NetworkHelper;
import com.norsecraft.common.network.s2c.CampfireFlexModelStateS2C;
import com.norsecraft.common.registry.NCBlockEntities;
import com.norsecraft.common.registry.NCItems;
import com.norsecraft.common.util.BlockEntityUtil;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CampfireBlock extends AbstractFurnaceBlock {

    public CampfireBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.combineAndSimplify(Block.createCuboidShape(2, 0, 2, 14, 1, 14),
                Block.createCuboidShape(5, 1, 5, 11, 4, 11), BooleanBiFunction.OR);
    }

    @Override
    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        if (player.getInventory().getMainHandStack().getItem() == NCItems.CAMPFIRE_STAND) {
            CampfireBlockEntity cbe = BlockEntityUtil.getBlockEntity(CampfireBlockEntity.class, world, pos);
            if(cbe.getModelState() == CampfireBlockEntity.State.DEFAULT) {
                NetworkHelper.broadcastToClient((ServerWorld) world, CampfireFlexModelStateS2C.PACKET_ID, new CampfireFlexModelStateS2C(pos, CampfireBlockEntity.State.WITH_STAND));
                player.getInventory().removeOne(player.getInventory().getMainHandStack());
                return;
            }
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CampfireBlockEntity) {
            player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, NCBlockEntities.campfireBlockEntity, CampfireBlockEntity::tick);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CampfireBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

}
