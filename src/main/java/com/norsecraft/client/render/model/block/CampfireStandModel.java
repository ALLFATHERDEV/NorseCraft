package com.norsecraft.client.render.model.block;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.block.entity.DummyBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CampfireStandModel extends AnimatedGeoModel<DummyBlockEntity> {

    @Override
    public Identifier getModelLocation(DummyBlockEntity object) {
        return NorseCraftMod.geoModel("campfire_stand.geo.json");
    }

    @Override
    public Identifier getTextureLocation(DummyBlockEntity object) {
        return NorseCraftMod.ncTex("block/campfire/campfire_stand.png");
    }

    @Override
    public Identifier getAnimationFileLocation(DummyBlockEntity animatable) {
        return null;
    }
}
