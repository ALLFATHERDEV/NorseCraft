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

    public boolean isFormed() {
        return this.hasMultiblock() && this.multiblock.getShape().isFormed();
    }

    public void unset(BlockPos pos) {
        if(!this.hasMultiblock())
            return;
        this.multiblock.unset(pos);
    }

    public abstract void scanForMultiblockAndSet(BlockPos pos, World world);

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
