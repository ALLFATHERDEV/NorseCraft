package com.norsecraft.common.entity.dwarf;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class DwarfWiseEntity extends DwarfEntity{

    public DwarfWiseEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
    }
}
