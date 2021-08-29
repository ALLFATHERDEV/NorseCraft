package com.norsecraft.common.block.entity;

import com.norsecraft.common.block.dwarfforge.BaseDwarfForgeBlock;
import com.norsecraft.common.block.dwarfforge.DwarfForgeMultiblock;
import com.norsecraft.common.block.multiblock.MultiblockShape;
import com.norsecraft.common.registry.NCBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DwarfForgeBlockEntity extends MultiblockTileEntity<DwarfForgeMultiblock> {

    public DwarfForgeBlockEntity(BlockPos pos, BlockState state) {
        super(NCBlockEntities.dwarfForgeEntity, pos, state);
    }

    @Override
    public void scanForMultiblockAndSet(BlockPos pos, World world) {
        BlockPos[] neighbors = this.getNeighbors(pos);
        for(BlockPos neighbor : neighbors) {
            BlockEntity be = world.getBlockEntity(neighbor);
            if(be instanceof DwarfForgeBlockEntity otherTile) {
                if(otherTile.multiblock != null) {
                    this.setMultiblock(otherTile.multiblock);
                    MultiblockShape shape = this.multiblock.getShape();
                    if(shape != null)
                        this.multiblock.set(shape.getControllerPos(), world.getBlockState(shape.getControllerPos()).get(BaseDwarfForgeBlock.HORIZONTAL_FACING), world);

                }
            }
        }
    }
}
