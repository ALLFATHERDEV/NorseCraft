package com.norsecraft.client.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

public interface RenderNCOverlayCallback {

    Event<RenderNCOverlayCallback> EVENT = EventFactory.createArrayBacked(RenderNCOverlayCallback.class,
            (listeners) -> (matrix) -> {
                for (RenderNCOverlayCallback listener : listeners) {
                    ActionResult result = listener.onRender(matrix);
                    if (result != ActionResult.PASS)
                        return result;
                }
                return ActionResult.PASS;
            });

    ActionResult onRender(MatrixStack matrix);


}
