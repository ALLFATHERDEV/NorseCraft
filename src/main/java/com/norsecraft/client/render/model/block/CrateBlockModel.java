package com.norsecraft.client.render.model.block;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.block.entity.CrateBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CrateBlockModel extends AnimatedGeoModel<CrateBlockEntity> {

    @Override
    public Identifier getModelLocation(CrateBlockEntity object) {
        return NorseCraftMod.geoModel("crate.geo.json");
    }

    @Override
    public Identifier getTextureLocation(CrateBlockEntity object) {
        return NorseCraftMod.ncTex("block/crate.png");
    }

    @Override
    public Identifier getAnimationFileLocation(CrateBlockEntity animatable) {
        return NorseCraftMod.nc("animations/crate.animation.json");
    }
}
