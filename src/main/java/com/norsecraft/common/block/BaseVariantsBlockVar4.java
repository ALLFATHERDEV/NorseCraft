package com.norsecraft.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BaseVariantsBlockVar4 extends Block {

    public static final IntProperty VARIANTS_4 = IntProperty.of("block_variants", 1, 4);

    public BaseVariantsBlockVar4(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(VARIANTS_4, new Random().nextInt(4) + 1));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(VARIANTS_4);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(VARIANTS_4, new Random().nextInt(4) + 1);
    }
}
