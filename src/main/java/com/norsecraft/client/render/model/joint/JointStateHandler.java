package com.norsecraft.client.render.model.joint;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public interface JointStateHandler<E extends IAnimatable> {

    int[] getStates();

    int getCurrentState();

    void next();

    void back();

    AnimatedGeoModel<E> getCurrentModel();

}
