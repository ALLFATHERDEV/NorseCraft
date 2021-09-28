package com.norsecraft.common.world.feature;

import com.mojang.serialization.Codec;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.world.feature.config.NCDecorationFeatureConfig;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class NCRocksFeature extends Feature<NCDecorationFeatureConfig> {

    public NCRocksFeature(Codec<NCDecorationFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<NCDecorationFeatureConfig> context) {
        BlockPos pos = context.getWorld().getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, context.getOrigin());
        NCDecorationFeatureConfig config = context.getConfig();
        Block block = config.block().getBlockState(context.getRandom(), pos).getBlock();
        NorseCraftMod.LOGGER.info("Pos: {}", pos);
        context.getWorld().setBlockState(pos, block.getPlacementState(null), 3);
        return true;
    }
}
