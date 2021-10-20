package com.norsecraft.client.render.model.joint;

import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import java.awt.*;

public class JointModelRenderer<E extends BlockEntity & IAnimatable & JointStateHandler<E>> implements IGeoRenderer<E>, BlockEntityRenderer<E> {

    static {
        AnimationController.addModelFetcher((IAnimatable object) -> {
            if (object instanceof BlockEntity) {
                BlockEntity tile = (BlockEntity) object;
                BlockEntityRenderer<BlockEntity> renderer = MinecraftClient.getInstance().getBlockEntityRenderDispatcher().get(tile);
                if (renderer instanceof GeoBlockRenderer) {
                    return (IAnimatableModel<Object>) ((GeoBlockRenderer<?>) renderer).getGeoModelProvider();
                }
            }
            return null;
        });
    }

    private JointModel<E> jointModel;

    public JointModelRenderer() {

    }

    @Override
    public void render(BlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        E e = (E) entity;
        if(this.jointModel == null)
            this.jointModel = e.getJointModel();
        this.jointModel.handle();
        this.render(e, tickDelta, matrices, vertexConsumers, light);
    }

    public void render(E tile, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn,
                       int packedLightIn) {
        AnimatedGeoModel<E> currentModel = this.jointModel.getCurrentModel();
        GeoModel geoModel = currentModel.getModel(currentModel.getModelLocation(tile));
        currentModel.setLivingAnimations(tile, this.getUniqueID(tile));
        stack.push();
        stack.translate(0, 0.01f, 0);
        stack.translate(0.5, 0, 0.5);

        rotateBlock(getFacing(tile), stack);

        MinecraftClient.getInstance().getTextureManager().bindTexture(getTextureLocation(tile));
        Color renderColor = getRenderColor(tile, partialTicks, stack, bufferIn, null, packedLightIn);
        RenderLayer renderType = getRenderType(tile, partialTicks, stack, bufferIn, null, packedLightIn, getTextureLocation(tile));
        render(geoModel, tile, partialTicks, renderType, stack, bufferIn, null, packedLightIn, OverlayTexture.DEFAULT_UV,
                (float) renderColor.getRed() / 255f, (float) renderColor.getGreen() / 255f,
                (float) renderColor.getBlue() / 255f, (float) renderColor.getAlpha() / 255f);
        stack.pop();

    }

    protected void rotateBlock(Direction facing, MatrixStack stack) {
        switch (facing) {
            case SOUTH -> stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
            case WEST -> stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
            case NORTH -> stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(0));
            case EAST -> stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(270));
            case UP -> stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
            case DOWN -> stack.multiply(Vec3f.NEGATIVE_X.getDegreesQuaternion(90));
        }
    }

    private Direction getFacing(E tile) {
        BlockState blockState = tile.getCachedState();
        if (blockState.contains(HorizontalFacingBlock.FACING)) {
            return blockState.get(HorizontalFacingBlock.FACING);
        } else if (blockState.contains(FacingBlock.FACING)) {
            return blockState.get(FacingBlock.FACING);
        } else {
            return Direction.NORTH;
        }
    }

    @Override
    public AnimatedGeoModel<E> getGeoModelProvider() {
        return this.jointModel.getCurrentModel();
    }

    @Override
    public Identifier getTextureLocation(E instance) {
        return this.jointModel.getCurrentModel().getTextureLocation(instance);
    }

}
