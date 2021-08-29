package com.norsecraft.client.render.model.entity;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.entity.dwarf.DwarfWiseEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DwarfWiseEntityModel extends AnimatedGeoModel<DwarfWiseEntity> {

    @Override
    public Identifier getModelLocation(DwarfWiseEntity object) {
        return NorseCraftMod.geoModel("dwarf_wise.geo.json");
    }

    @Override
    public Identifier getTextureLocation(DwarfWiseEntity object) {
        return NorseCraftMod.ncTex("entity/dwarf_wise.png");
    }

    @Override
    public Identifier getAnimationFileLocation(DwarfWiseEntity animatable) {
        return NorseCraftMod.nc("animations/dwarf.animation.json");
    }
}
