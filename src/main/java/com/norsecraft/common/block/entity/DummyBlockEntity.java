package com.norsecraft.common.block.entity;

import com.norsecraft.common.registry.NCBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class DummyBlockEntity extends BlockEntity implements IAnimatable {

    private AnimationFactory animationFactory = new AnimationFactory(this);

    public DummyBlockEntity(BlockPos pos, BlockState state) {
        super(NCBlockEntities.dummy, pos, state);
    }

    @Override
    public void registerControllers(AnimationData animationData) {

    }

    @Override
    public AnimationFactory getFactory() {
        return animationFactory;
    }

    public static DummyBlockEntity createDummy(BlockEntity parent) {
        return new DummyBlockEntity(parent.getPos(), parent.getCachedState());
    }
}
