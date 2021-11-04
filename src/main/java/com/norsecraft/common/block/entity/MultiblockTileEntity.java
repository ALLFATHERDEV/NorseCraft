package com.norsecraft.common.block.entity;

import com.norsecraft.common.block.multiblock.IMultiblock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.swing.*;

/**
 * This is a base blockentity for multiblocks. Every block entity for multiblocks should be extend from the blockentity
 *
 * @param <T>
 */
public abstract class MultiblockTileEntity<T extends IMultiblock> extends BlockEntity {

    protected T multiblock;

    public MultiblockTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void setMultiblock(T multiblock) {
        this.multiblock = multiblock;
    }

    public boolean hasMultiblock() {
        return this.multiblock != null;
    }

    /**
     * @return true if the multiblock is formed or false if not
     */
    public boolean isFormed() {
        return this.hasMultiblock() && this.multiblock.getShape().isFormed();
    }

    public void unset(BlockPos pos) {
        if(!this.hasMultiblock())
            return;
        this.multiblock.unset(pos);
    }

    /**
     * This method is called when the player updates or destroyed a block.
     * If the player place repair the multiblock this method will reactivate the multiblock
     *
     * @param pos the updated pos
     * @param world the world
     */
    public abstract void scanForMultiblockAndSet(BlockPos pos, World world);

    /**
     * @param pos the block pos
     * @return the neighbors of the block
     */
    public BlockPos[] getNeighbors(BlockPos pos) {
        BlockPos[] neighbors = new BlockPos[4];
        neighbors[0] = pos.offset(Direction.NORTH);
        neighbors[1] = pos.offset(Direction.EAST);
        neighbors[2] = pos.offset(Direction.SOUTH);
        neighbors[3] = pos.offset(Direction.WEST);
        return neighbors;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        if(multiblock != null) {
            nbt.put("Multiblock", this.multiblock.write());
        }
        return super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if(nbt.contains("Multiblock")) {
            this.multiblock = IMultiblock.read(nbt);
        }
    }
}
