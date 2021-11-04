package com.norsecraft.client.render.model.flex;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class FlexModelRenderer<E extends BlockEntity & IAnimatable & FlexModelStateProvider> extends GeoBlockRenderer<E> {

    private final FlexModel<E> flexModel;

    public FlexModelRenderer(FlexModel<E> flexModel) {
        super(flexModel);
        this.flexModel = flexModel;
    }

    @Override
    public void renderEarly(E animatable, MatrixStack stackIn, float ticks, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder,
                            int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        if (animatable.shouldPickNextModel())
            flexModel.increase(animatable);
        else if (animatable.shouldPickBackModel())
            flexModel.decrease(animatable);
    }
}
