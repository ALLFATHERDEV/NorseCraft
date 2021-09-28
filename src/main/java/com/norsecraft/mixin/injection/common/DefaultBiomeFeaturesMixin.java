package com.norsecraft.mixin.injection.common;

import com.norsecraft.common.registry.NCConfiguredFeatures;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefaultBiomeFeatures.class)
public class DefaultBiomeFeaturesMixin {

    @Inject(method = "addDefaultOres", at = @At("TAIL"))
    private static void addPlainsFeature(GenerationSettings.Builder buidler, CallbackInfo ci) {
        buidler.feature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, NCConfiguredFeatures.STICKS_DECORATION);
        buidler.feature(GenerationStep.Feature.UNDERGROUND_ORES, NCConfiguredFeatures.ORE_BRONZE);
        buidler.feature(GenerationStep.Feature.UNDERGROUND_ORES, NCConfiguredFeatures.ORE_COPPER);
    }

}
