package com.norsecraft.client.render.model.entity;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.entity.dwarf.DwarfEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DwarfEntityModel extends AnimatedGeoModel<DwarfEntity> {
    @Override
    public Identifier getModelLocation(DwarfEntity object) {
        return NorseCraftMod.geoModel(object.getModelVar().getGeoPath());
    }

    @Override
    public Identifier getTextureLocation(DwarfEntity object) {
        return NorseCraftMod.ncTex(object.getTextureVar().getTexPath());
    }

    @Override
    public Identifier getAnimationFileLocation(DwarfEntity animatable) {
        return NorseCraftMod.nc("animations/dwarf.animation.json");
    }
}
