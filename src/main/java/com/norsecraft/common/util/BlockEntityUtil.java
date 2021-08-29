package com.norsecraft.common.util;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;

public class BlockEntityUtil {

    public static <T extends BlockEntity> T getBlockEntity(Class<T> type, World world, PacketByteBuf buf) {
        BlockEntity be = world.getBlockEntity(buf.readBlockPos());
        if (type.isInstance(be)) {
            return (T) be;
        }
        throw new IllegalArgumentException("Block entity not found. Type: " + type);
    }

}
