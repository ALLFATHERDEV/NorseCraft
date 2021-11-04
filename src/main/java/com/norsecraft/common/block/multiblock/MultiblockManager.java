package com.norsecraft.common.block.multiblock;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class MultiblockManager {

    /**
     * All saved multiblocks
     */
    private final Map<Identifier, IMultiblock> multiblocks;

    public MultiblockManager() {
        this.multiblocks = Maps.newHashMap();
    }

    /**
     * Add a new multiblock
     * @param multiblock the new multiblock
     */
    public void addMultiblock(IMultiblock multiblock) {
        Preconditions.checkNotNull(multiblock);
        if(multiblocks.containsKey(multiblock.getId()))
            return;
        multiblocks.put(multiblock.getId(), multiblock);
    }

    /**
     * @param id the multiblock id
     * @return the multiblock or null if it is not registered
     */
    @Nullable
    public IMultiblock getMultiblock(Identifier id) {
        return this.multiblocks.get(id).cloneMultiblock();
    }


}
