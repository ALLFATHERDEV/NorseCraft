package com.norsecraft.common.registry;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.world.feature.config.NCDecorationFeatureConfig;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;

public class NCConfiguredFeatures {

    //STICKS
    public static final ConfiguredFeature<?, ?> STICKS_DECORATION = NCFeatures.NC_DECORATION.configure(new NCDecorationFeatureConfig(new SimpleBlockStateProvider(NCBlocks.STICKS.getDefaultState())))
            .applyChance(4);
    public static final RegistryKey<ConfiguredFeature<?, ?>> STICKS_DECORATION_KEY = registerKey("nc_deco_sticks");

    //ORES
    public static final ConfiguredFeature<?, ?> ORE_COPPER = Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
                    NCBlocks.COPPER_ORE.getDefaultState(),
                    9))
            .range(new RangeDecoratorConfig(UniformHeightProvider.create(YOffset.aboveBottom(0), YOffset.fixed(64))))
            .spreadHorizontally()
            .repeat(20);
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_COPPER_KEY = registerKey("nc_ore_copper");

    public static final ConfiguredFeature<?, ?> ORE_BRONZE = Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
                    NCBlocks.COPPER_ORE.getDefaultState(),
                    9))
            .range(new RangeDecoratorConfig(UniformHeightProvider.create(YOffset.aboveBottom(0), YOffset.fixed(64))))
            .spreadHorizontally()
            .repeat(20);
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_BRONZE_KEY = registerKey("nc_ore_bronze");

    public static final ConfiguredFeature<?, ?> ORE_IRITHIUM = Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
                    NCBlocks.COPPER_ORE.getDefaultState(),
                    3))
            .range(new RangeDecoratorConfig(UniformHeightProvider.create(YOffset.aboveBottom(0), YOffset.fixed(35))))
            .spreadHorizontally()
            .repeat(5);
    public static final RegistryKey<ConfiguredFeature<?, ?>> ORE_IRITHIUM_KEY = registerKey("nc_ore_irithium");


    public static void register() {
        register(STICKS_DECORATION_KEY, STICKS_DECORATION);
        register(ORE_BRONZE_KEY, ORE_BRONZE);
        register(ORE_COPPER_KEY, ORE_COPPER);
        register(ORE_IRITHIUM_KEY, ORE_IRITHIUM);
    }

    private static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, NorseCraftMod.nc(name));
    }

    private static <T extends ConfiguredFeature<?, ?>> void register(RegistryKey<ConfiguredFeature<?, ?>> key, ConfiguredFeature<?, ?> configuredFeature) {
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, key.getValue(), configuredFeature);
    }

}
