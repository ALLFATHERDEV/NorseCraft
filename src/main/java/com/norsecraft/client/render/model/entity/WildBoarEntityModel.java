package com.norsecraft.client.render.model.entity;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.entity.animal.WildBoarEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class WildBoarEntityModel extends AnimatedGeoModel<WildBoarEntity> {
    @Override
    public Identifier getModelLocation(WildBoarEntity object) {
        return NorseCraftMod.geoModel("wild_boar.geo.json");
    }

    @Override
    public Identifier getTextureLocation(WildBoarEntity object) {
        return NorseCraftMod.ncTex(("entity/wild_boar.png"));
    }

    @Override
    public Identifier getAnimationFileLocation(WildBoarEntity animatable) {
        return NorseCraftMod.nc("animations/boar.animation.json");
    }
}
