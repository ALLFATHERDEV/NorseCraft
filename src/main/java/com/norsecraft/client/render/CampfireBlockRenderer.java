package com.norsecraft.client.render;

import com.norsecraft.client.render.model.block.CampfireBlockModel;
import com.norsecraft.client.render.model.block.CampfireStandModel;
import com.norsecraft.common.block.entity.CampfireBlockEntity;
import com.norsecraft.common.block.entity.DummyBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class CampfireBlockRenderer extends GeoBlockRenderer<CampfireBlockEntity> {

    private CampfireStandRenderer campfireStandRenderer;
    private DummyBlockEntity dummy;

    public CampfireBlockRenderer() {
        super(new CampfireBlockModel());
        this.campfireStandRenderer = new CampfireStandRenderer();
    }

    @Override
    public void renderLate(CampfireBlockEntity animatable, MatrixStack stackIn, float ticks, VertexConsumerProvider renderTypeBuffer,
                           VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        if (animatable.isActive()) {
            if (dummy == null)
                dummy = DummyBlockEntity.createDummy(animatable);
            campfireStandRenderer.render(dummy, ticks, stackIn, renderTypeBuffer, packedLightIn, packedOverlayIn);
        }
    }

    @Override
    public RenderLayer getRenderType(CampfireBlockEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }

    public static class CampfireStandRenderer extends GeoBlockRenderer<DummyBlockEntity> {

        public CampfireStandRenderer() {
            super(new CampfireStandModel());
        }

        @Override
        public void render(DummyBlockEntity tile, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
            stack.push();
            stack.translate(-0.5, 0, -0.5);
            super.render(tile, partialTicks, stack, bufferIn, packedLightIn);
            stack.pop();
        }

        @Override
        public RenderLayer getRenderType(DummyBlockEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
            return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
        }
    }

}
