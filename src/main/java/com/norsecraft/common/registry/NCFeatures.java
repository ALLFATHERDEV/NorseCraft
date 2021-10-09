package com.norsecraft.common.registry;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.world.feature.NCDecorationFeature;
import com.norsecraft.common.world.feature.NCRocksFeature;
import com.norsecraft.common.world.feature.config.NCDecorationFeatureConfig;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;

/**
 * Register all the features for the mod
 */
public class NCFeatures {

    public static final Feature<NCDecorationFeatureConfig> NC_DECORATION = new NCDecorationFeature(NCDecorationFeatureConfig.CODEC);

    public static void register() {
        register("nc_decoration", NC_DECORATION);
    }

    private static <T extends Feature<?>> void register(String name, T feature) {
        Registry.register(Registry.FEATURE, NorseCraftMod.nc(name), feature);
    }

}
