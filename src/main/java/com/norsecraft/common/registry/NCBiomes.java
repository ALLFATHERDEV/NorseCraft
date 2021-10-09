package com.norsecraft.common.registry;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.world.biome.DwarfBiome;
import com.norsecraft.mixin.accessor.BuiltinBiomesAccessor;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;

/**
 * All custom biomes
 */
public class NCBiomes {

    public static final RegistryKey<Biome> DWARF_BIOME = registerKey("dwarf_biome");

    public static void register() {
        registerBiome(DWARF_BIOME, DwarfBiome.BIOME, "dwarfs", DwarfBiome.DWARF_SURFACE_BUILDER);
    }

    private static void registerBiome(RegistryKey<Biome> key, Biome biome, String surfaceBuilderName, ConfiguredSurfaceBuilder<?> surfaceBuilder) {
        Registry.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, NorseCraftMod.nc(surfaceBuilderName), surfaceBuilder);
        Registry.register(BuiltinRegistries.BIOME, key.getValue(), biome);
        BuiltinBiomesAccessor.getRawIdMap().put(BuiltinRegistries.BIOME.getRawId(biome), key);
    }

    private static RegistryKey<Biome> registerKey(String name) {
        return RegistryKey.of(Registry.BIOME_KEY, NorseCraftMod.nc(name));
    }

}
