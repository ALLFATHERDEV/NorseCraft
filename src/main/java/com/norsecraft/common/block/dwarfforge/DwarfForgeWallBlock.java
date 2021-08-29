package com.norsecraft.common.block.dwarfforge;

import com.norsecraft.common.util.VoxelShapeGroups;

public class DwarfForgeWallBlock extends BaseDwarfForgeDoubleBlock {

    public DwarfForgeWallBlock() {
        super(VoxelShapeGroups.DwarfForge.DWARF_FORGE_WALL_BOTTOM_SHAPES, VoxelShapeGroups.DwarfForge.DWARF_FORGE_WALL_TOP_SHAPES);
    }

}
