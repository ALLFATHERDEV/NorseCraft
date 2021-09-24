package com.norsecraft.client.render.model.entity;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.entity.dwarf.DwarfWarriorEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DwarfWarriorEntityModel extends AnimatedGeoModel<DwarfWarriorEntity> {
    @Override
    public Identifier getModelLocation(DwarfWarriorEntity object) {
        return NorseCraftMod.geoModel("dwarf_warrior_var2.geo.json");
    }

    @Override
    public Identifier getTextureLocation(DwarfWarriorEntity object) {
        return NorseCraftMod.ncTex("entity/dwarf_warrior1_var2.png");
    }

    @Override
    public Identifier getAnimationFileLocation(DwarfWarriorEntity animatable) {
        return NorseCraftMod.nc("animations/dwarf.animation.json");
    }
}
