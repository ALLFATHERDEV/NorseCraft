package com.norsecraft.client.render.model.joint;

import software.bernie.geckolib3.core.IAnimatable;

public interface JointModelProvider<E extends IAnimatable> {

    JointStateHandler<E> getJointStateHandler();

}
