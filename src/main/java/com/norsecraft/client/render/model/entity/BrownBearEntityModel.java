package com.norsecraft.client.render.model.entity;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.entity.animal.BrownBearEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BrownBearEntityModel extends AnimatedGeoModel<BrownBearEntity> {
    @Override
    public Identifier getModelLocation(BrownBearEntity object) {
        return NorseCraftMod.geoModel("brown_bear.geo.json");
    }

    @Override
    public Identifier getTextureLocation(BrownBearEntity object) {
        return NorseCraftMod.ncTex("entity/brown_bear.png");
    }

    @Override
    public Identifier getAnimationFileLocation(BrownBearEntity animatable) {
        return NorseCraftMod.nc("animations/brown_bear.animation.json");
    }
}
