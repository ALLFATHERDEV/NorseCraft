package com.norsecraft.common.entity.dwarf;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class DwarfEntity extends AbstractDwarfEntity {

    public DwarfEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
        this.setRandomModel();
    }


}
