package com.norsecraft.client.render.model.block.campfire;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.block.entity.CampfireBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CampfireWithStandAndBowlModel extends AnimatedGeoModel<CampfireBlockEntity> {
    @Override
    public Identifier getModelLocation(CampfireBlockEntity object) {
        return NorseCraftMod.geoModel("campfire_with_stand_and_bowl.geo.json");
    }

    @Override
    public Identifier getTextureLocation(CampfireBlockEntity object) {
        return NorseCraftMod.ncTex("block/campfire/campfire_with_stand_and_bowl.png");
    }

    @Override
    public Identifier getAnimationFileLocation(CampfireBlockEntity animatable) {
        return null;
    }
}
