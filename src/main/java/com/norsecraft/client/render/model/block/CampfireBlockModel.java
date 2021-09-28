package com.norsecraft.client.render.model.block;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.block.entity.CampfireBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CampfireBlockModel extends AnimatedGeoModel<CampfireBlockEntity> {
    @Override
    public Identifier getModelLocation(CampfireBlockEntity object) {
        return NorseCraftMod.geoModel("campfire.geo.json");
    }

    @Override
    public Identifier getTextureLocation(CampfireBlockEntity object) {
        return NorseCraftMod.ncTex("block/campfire/campfire.png");
    }

    @Override
    public Identifier getAnimationFileLocation(CampfireBlockEntity animatable) {
        return null;
    }
}
