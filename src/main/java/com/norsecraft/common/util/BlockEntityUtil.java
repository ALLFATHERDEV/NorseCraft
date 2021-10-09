package com.norsecraft.common.util;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.World;

/**
 * A helper class for block entities
 */
public class BlockEntityUtil {

    /**
     * @param type the block entity type
     * @param world the world
     * @param buf the bytebuf where the data is been stored
     * @param <T> the block entity type
     * @return the block entity from the block or throws a {@link IllegalArgumentException} if no block entity was found
     */
    public static <T extends BlockEntity> T getBlockEntity(Class<T> type, World world, PacketByteBuf buf) {
        BlockEntity be = world.getBlockEntity(buf.readBlockPos());
        if (type.isInstance(be)) {
            return (T) be;
        }
        throw new IllegalArgumentException("Block entity not found. Type: " + type);
    }

}
