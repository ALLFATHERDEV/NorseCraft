package com.norsecraft.common.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record NCDecorationFeatureConfig(BlockStateProvider block) implements FeatureConfig {

    public static final Codec<NCDecorationFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("block").forGetter(NCDecorationFeatureConfig::block)
    ).apply(instance, instance.stable(NCDecorationFeatureConfig::new)));

}
