package com.norsecraft.common.util;

import net.minecraft.util.shape.VoxelShape;

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
