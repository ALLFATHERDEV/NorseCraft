package com.norsecraft.common.block.multiblock;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class MultiblockManager {

    private final Map<Identifier, IMultiblock> multiblocks;

    public MultiblockManager() {
        this.multiblocks = Maps.newHashMap();
    }

    public void addMultiblock(IMultiblock multiblock) {
        Preconditions.checkNotNull(multiblock);
        if(multiblocks.containsKey(multiblock.getId()))
            return;
        multiblocks.put(multiblock.getId(), multiblock);
    }

    @Nullable
    public IMultiblock getMultiblock(Identifier id) {
        return this.multiblocks.get(id).cloneMultiblock();
    }


}
