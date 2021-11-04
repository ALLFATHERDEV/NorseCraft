package com.norsecraft.client.render.model.flex;

import net.minecraft.util.Identifier;

public interface FlexModelStateProvider {

    boolean shouldPickNextModel();

    boolean shouldPickBackModel();

    void setStopNext(boolean next);

    void setStopBack(boolean back);

    Identifier[] getModels();

    Identifier[] getTextures();

    Identifier[] getAnimations();

}
