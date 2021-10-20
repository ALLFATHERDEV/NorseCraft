package com.norsecraft.client.render.model.joint;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

/**
 * This handler handles the different states for the models.
 * You can set here, which model should be rendered at the moment
 *
 * @param <E> the block entity class
 */
public interface JointStateHandler<E extends IAnimatable> {

    /**
     * @return true if you want to render the next model otherwise false
     */
    boolean canGoNext();

    /**
     * @return true if you want to render the previous model otherwise false
     */
    boolean canGoBack();

    /**
     * Handles the state change for next
     * Called after canGoNext
     */
    void next();

    /**
     * Handles the state change for back
     * Called after canGoBack
     */
    void back();

    /**
     * @return the current model that should be rendered
     */
    AnimatedGeoModel<E> getCurrentModel();

    /**
     * @return the joint model that you created in the block entity class
     */
    JointModel<E> getJointModel();

}
