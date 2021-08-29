package com.norsecraft.common.block.multiblock;

import com.google.common.collect.Maps;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.registry.NCBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.function.Supplier;

public class MultiblockPrefabMatrix {

    public static final Identifier ID_DWARF_FORGE = NorseCraftMod.nc("dwarf_forge");

    public static final Map<Identifier, MultiblockPrefabMatrix> MATRIX_MAP = Maps.newHashMap();

    static {
        MATRIX_MAP.put(ID_DWARF_FORGE,
                MultiblockPrefabMatrix.startBuildMatrix(ID_DWARF_FORGE)
                        .add(new BlockPos(0, 0, 0), () -> NCBlocks.DWARF_FORGE_PILLAR, true)
                        .add(new BlockPos(1, 0, 0), () -> NCBlocks.DWARF_FORGE_WALL, true)
                        .add(new BlockPos(2, 0, 0), () -> NCBlocks.DWARF_FORGE_PILLAR, true)
                        .add(new BlockPos(1, 2, 0), () -> NCBlocks.DWARF_FORGE_CHIMNEY_WALL, true)
                        .add(new BlockPos(0, 0, 1), () -> NCBlocks.DWARF_FORGE_WALL, true)
                        .add(new BlockPos(0, 0, 2), () -> NCBlocks.DWARF_FORGE_PILLAR, true)
                        .add(new BlockPos(0, 2, 1), () -> NCBlocks.DWARF_FORGE_CHIMNEY_WALL, true)
                        .add(new BlockPos(1, 0, 1), () -> NCBlocks.DWARF_FORGE_PIT, false)
                        .add(new BlockPos(2, 0, 1), () -> NCBlocks.DWARF_FORGE_WALL, true)
                        .add(new BlockPos(2, 2, 1), () -> NCBlocks.DWARF_FORGE_CHIMNEY_WALL, true)
                        .add(new BlockPos(1, 0, 2), () -> NCBlocks.DWARF_FORGE_WALL, true)
                        .add(new BlockPos(1, 2, 2), () -> NCBlocks.DWARF_FORGE_CHIMNEY_WALL, true)
                        .add(new BlockPos(2, 0, 2), () -> NCBlocks.DWARF_FORGE_PILLAR, true));
    }

    private final Map<BlockPos, MatrixEntry> entries;
    private final Identifier id;

    private MultiblockPrefabMatrix(Identifier id) {
        this.id = id;
        this.entries = Maps.newHashMap();
    }

    public static MultiblockPrefabMatrix startBuildMatrix(Identifier id) {
        return new MultiblockPrefabMatrix(id);
    }

    public MultiblockPrefabMatrix add(BlockPos pos, Supplier<Block> block, boolean doubleBlock) {
        if (this.entries.containsKey(pos))
            this.entries.replace(pos, new MatrixEntry(pos, block, doubleBlock));
        else
            this.entries.put(pos, new MatrixEntry(pos, block, doubleBlock));
        return this;
    }

    public Map<BlockPos, MatrixEntry> getEntries() {
        return entries;
    }

    public Identifier getId() {
        return id;
    }

    public static class MatrixEntry {

        public final BlockPos pos;
        public final Supplier<Block> block;
        public final boolean doubleBlock;

        MatrixEntry(BlockPos pos, Supplier<Block> block, boolean doubleBlock) {
            this.pos = pos;
            this.block = block;
            this.doubleBlock = doubleBlock;
        }

    }

}
