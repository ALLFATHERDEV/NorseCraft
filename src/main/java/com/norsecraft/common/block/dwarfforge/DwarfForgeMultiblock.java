package com.norsecraft.common.block.dwarfforge;

import com.google.common.collect.Lists;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.block.entity.DwarfForgeBlockEntity;
import com.norsecraft.common.block.multiblock.IMultiblock;
import com.norsecraft.common.block.multiblock.MultiblockPrefabMatrix;
import com.norsecraft.common.block.multiblock.MultiblockShape;
import com.norsecraft.common.registry.NCBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

public class DwarfForgeMultiblock implements IMultiblock {

    public static final Identifier ID = NorseCraftMod.nc("dwarf_forge");

    private MultiblockShape shape;

    public DwarfForgeMultiblock() {
        this.shape = new MultiblockShape(new BlockPos(3, 4, 3), () -> Lists.newArrayList(
                NCBlocks.DWARF_FORGE_PIT, NCBlocks.DWARF_FORGE_PILLAR, NCBlocks.DWARF_FORGE_WALL, NCBlocks.DWARF_FORGE_CHIMNEY_WALL
        ), MultiblockPrefabMatrix.MATRIX_MAP.get(MultiblockPrefabMatrix.ID_DWARF_FORGE));
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MultiblockShape getShape() {
        return this.shape;
    }

    @Override
    public void set(BlockPos controllerPos, Direction blockDirection, World world) {
        if(this.shape.isShapeValid(controllerPos, blockDirection, world)) {
            List<MultiblockShape.ShapeBlockData> datas = this.shape.getShapeBlocKData();
            for(MultiblockShape.ShapeBlockData data : datas) {
                BlockEntity be = world.getBlockEntity(data.pos);
                if(be instanceof DwarfForgeBlockEntity) {
                    ((DwarfForgeBlockEntity) be).setMultiblock(this);
                }
            }
            this.shape.setFormed(true);
        }
    }

    @Override
    public void update(BlockPos pos) {
        List<MultiblockShape.ShapeBlockData> data = this.shape.getShapeBlocKData();
        if (data.stream().anyMatch(sbd -> sbd.pos.equals(pos))) {
            MultiblockShape.ShapeBlockData sbd = data.stream().filter(sbd0 -> sbd0.pos.equals(pos)).findFirst().get();
            data.remove(sbd);
            this.shape.setFormed(false);
        }
    }

    @Override
    public void unset(BlockPos pos) {
        List<MultiblockShape.ShapeBlockData> data = this.shape.getShapeBlocKData();
        if (data.stream().anyMatch(sbd -> sbd.pos.equals(pos))) {
            MultiblockShape.ShapeBlockData sbd = data.stream().filter(sbd0 -> sbd0.pos.equals(pos)).findFirst().get();
            data.remove(sbd);
        }
        this.shape.setFormed(false);
    }

    @Override
    public NbtCompound write() {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("ID", this.getId().getPath());
        nbt.put("Shape", this.shape.write());
        return nbt;
    }

    @Override
    public IMultiblock cloneMultiblock() {
        return (IMultiblock) this.clone();
    }

    @Override
    protected Object clone() {
        return new DwarfForgeMultiblock();
    }

    @Override
    public void setShape(MultiblockShape shape) {
        this.shape = shape;
    }
}
