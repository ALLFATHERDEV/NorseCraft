package com.norsecraft.common.world;

import com.norsecraft.common.registry.NCConfiguredFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;

public class BiomeFeatureInitializer {

    public static void initialize() {
        BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Feature.SURFACE_STRUCTURES, NCConfiguredFeatures.ROCKS_DECORATION_KEY);
        BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Feature.SURFACE_STRUCTURES, NCConfiguredFeatures.STICKS_DECORATION_KEY);

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, NCConfiguredFeatures.ORE_BRONZE_KEY);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, NCConfiguredFeatures.ORE_COPPER_KEY);

    }

}
