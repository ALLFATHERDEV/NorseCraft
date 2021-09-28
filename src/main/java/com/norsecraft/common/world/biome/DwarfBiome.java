package com.norsecraft.common.world.biome;

import com.norsecraft.common.registry.NCBlocks;
import com.norsecraft.common.registry.NCConfiguredFeatures;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

public class DwarfBiome {

    public static final ConfiguredSurfaceBuilder<TernarySurfaceConfig> DWARF_SURFACE_BUILDER = SurfaceBuilder.DEFAULT
            .withConfig(new TernarySurfaceConfig(
                    NCBlocks.DARK_COBBLESTONE.getDefaultState(),
                    NCBlocks.DARK_STONE.getDefaultState(),
                    NCBlocks.DARK_STONE.getDefaultState()
            ));

    public static final Biome BIOME = createDwarfBiome();

    private static Biome createDwarfBiome() {

        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        DefaultBiomeFeatures.addMonsters(spawnSettings, 95, 2, 95);

        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
        generationSettings.surfaceBuilder(DWARF_SURFACE_BUILDER);
        generationSettings.feature(GenerationStep.Feature.UNDERGROUND_ORES, NCConfiguredFeatures.ORE_IRITHIUM);
        DefaultBiomeFeatures.addDefaultUndergroundStructures(generationSettings);
        DefaultBiomeFeatures.addLandCarvers(generationSettings);
        DefaultBiomeFeatures.addDungeons(generationSettings);
        DefaultBiomeFeatures.addMineables(generationSettings);
        DefaultBiomeFeatures.addDefaultOres(generationSettings);
        DefaultBiomeFeatures.addFrozenTopLayer(generationSettings);

        return new Biome.Builder()
                .precipitation(Biome.Precipitation.RAIN)
                .category(Biome.Category.NONE)
                .depth(0.16F)
                .scale(0.05F)
                .temperature(0.2F)
                .downfall(0.2F)
                .effects(new BiomeEffects.Builder()
                        .waterColor(0x3f76e4)
                        .waterFogColor(0x050533)
                        .fogColor(0xc0d8ff)
                        .skyColor(0x77adff)
                        .build())
                .spawnSettings(spawnSettings.build())
                .generationSettings(generationSettings.build())
                .build();
    }

}
