package com.norsecraft.client.render.model.joint;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class JointModel<E extends IAnimatable> {

    protected JointStateHandler<E> stateHandler;

    public JointModel(JointStateHandler<E> stateHandler) {
        this.stateHandler = stateHandler;
    }

    public void handle() {
        if(stateHandler.canGoNext())
            stateHandler.next();
        else if(stateHandler.canGoBack())
            stateHandler.back();
    }

    public AnimatedGeoModel<E> getCurrentModel() {
        return this.stateHandler.getCurrentModel();
    }

}
