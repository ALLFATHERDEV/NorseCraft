package com.norsecraft.client.render.entity;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.entity.dwarf.AbstractDwarfEntity;
import com.norsecraft.common.entity.dwarf.DwarfEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class DwarfEntityRenderer<E extends AbstractDwarfEntity> extends GeoEntityRenderer<E> {

    private final Identifier texture;

    public DwarfEntityRenderer(EntityRendererFactory.Context ctx, AnimatedGeoModel<E> modelProvider, Identifier texture) {
        super(ctx, modelProvider);
        this.texture = texture;
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if(bone.getName().equals("rightHand")) {
            stack.push();

            stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-75)); //def: -75
            stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(0));
            stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(0));

            //stack.translate(0.38D, 0.3D, 0.6D);
            stack.translate(0.38D, 0.3D, 0.3D);

            float scale = 1f;
            stack.scale(scale, scale, scale);

            MinecraftClient.getInstance().getItemRenderer().renderItem(Items.DIAMOND_SWORD.getDefaultStack(),
                    ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb, 0);

            stack.pop();
            bufferIn = rtb.getBuffer(RenderLayer.getEntityTranslucent(whTexture));
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public Identifier getTexture(E entity) {
        if(entity instanceof DwarfEntity) {
            return NorseCraftMod.ncTex(entity.getTextureVar().getTexPath());
        }
        return this.texture;
    }
}
