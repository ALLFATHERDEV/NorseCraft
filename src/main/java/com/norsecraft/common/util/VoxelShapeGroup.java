package com.norsecraft.common.util;

import net.minecraft.util.shape.VoxelShape;

/**
 * This class holds {@link VoxelShape}s for every facing side. North, east, south and west.
 * {@link VoxelShape}s are the bounding boxes of the blocks
 * If Martii sends us blocks, the voxel shapes are in the same folder. He created the {@link VoxelShape}s with Blockbench
 */
public class VoxelShapeGroup {

    public final VoxelShape north;
    public final VoxelShape east;
    public final VoxelShape south;
    public final VoxelShape west;

    public VoxelShapeGroup(VoxelShape north, VoxelShape east, VoxelShape south, VoxelShape west) {
        this.north = north;
        this.east = east;
        this.south = south;
        this.west = west;
    }

}
