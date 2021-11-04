package com.norsecraft.client.render.model.flex;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FlexModel<E extends IAnimatable & FlexModelStateProvider> extends AnimatedGeoModel<E> {

    private int currentModelIndex = 0;

    public void increase(E entity) {
        this.currentModelIndex++;
        entity.setStopNext(true);
    }

    public void decrease(E entity) {
        this.currentModelIndex--;
        entity.setStopBack(true);
    }

    @Override
    public Identifier getModelLocation(E object) {
        return object.getModels()[MathHelper.clamp(this.currentModelIndex, 0, object.getModels().length - 1)];
    }

    @Override
    public Identifier getTextureLocation(E object) {
        return object.getTextures()[MathHelper.clamp(this.currentModelIndex, 0, object.getTextures().length - 1)];
    }

    @Override
    public Identifier getAnimationFileLocation(E animatable) {
        if(animatable.getAnimations() != null)
            return animatable.getAnimations()[MathHelper.clamp(this.currentModelIndex, 0, animatable.getAnimations().length - 1)];
        return null;
    }
}
