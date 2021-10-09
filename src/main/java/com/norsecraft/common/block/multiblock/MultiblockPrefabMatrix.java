package com.norsecraft.common.block.multiblock;

import com.google.common.collect.Maps;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.registry.NCBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.function.Supplier;

/**
 * This class holds the structure data for the multiblock.
 * If you want to add a multiblock, you have to set in the static block the position for every block in the structure
 */
public class MultiblockPrefabMatrix {

    public static final Identifier ID_DWARF_FORGE = NorseCraftMod.nc("dwarf_forge");

    public static final Map<Identifier, MultiblockPrefabMatrix> MATRIX_MAP = Maps.newHashMap();

    static {
        //Here I build the finished multiblock structure for the dwarf forge.
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

    /**
     * Call this method if you want to start the prefab
     * @param id the multiblock id
     * @return a new prefab matrix where you can build the prefab
     */
    public static MultiblockPrefabMatrix startBuildMatrix(Identifier id) {
        return new MultiblockPrefabMatrix(id);
    }

    /**
     * Adds a entry to the prefab matrix, that represents a block
     * @param pos the normal block position
     * @param block the block
     * @param doubleBlock if the block is a double sized block set it to true otherwise false
     * @return the current prefab matrix
     */
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
