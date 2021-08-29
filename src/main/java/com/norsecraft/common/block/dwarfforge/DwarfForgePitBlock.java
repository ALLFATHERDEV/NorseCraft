package com.norsecraft.common.block.dwarfforge;

import com.norsecraft.common.util.VoxelShapeGroups;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;

public class DwarfForgePitBlock extends BaseDwarfForgeBlock {
    public DwarfForgePitBlock() {
        super(FabricBlockSettings.copyOf(Blocks.OBSIDIAN), VoxelShapeGroups.DwarfForge.DWARF_FORGE_PIT_SHAPES);
    }
}
