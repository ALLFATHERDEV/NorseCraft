package com.norsecraft.client.render.model.entity;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.entity.dwarf.DwarfBlacksmithEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DwarfBlacksmithEntityModel extends AnimatedGeoModel<DwarfBlacksmithEntity> {

    @Override
    public Identifier getModelLocation(DwarfBlacksmithEntity object) {
        return NorseCraftMod.geoModel("dwarf_blacksmith.geo.json");
    }

    @Override
    public Identifier getTextureLocation(DwarfBlacksmithEntity object) {
        return NorseCraftMod.ncTex("entity/dwarf_blacksmith.png");
    }

    @Override
    public Identifier getAnimationFileLocation(DwarfBlacksmithEntity animatable) {
        return NorseCraftMod.nc("animations/dwarf.animation.json");
    }

}
