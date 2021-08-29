package com.norsecraft.client.render.entity;

import com.norsecraft.client.render.model.entity.WildBoarEntityModel;
import com.norsecraft.common.entity.WildBoarEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class WildBoarEntityRenderer extends GeoEntityRenderer<WildBoarEntity> {

    public WildBoarEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new WildBoarEntityModel());
    }

    @Override
    public RenderLayer getRenderType(WildBoarEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }
}
