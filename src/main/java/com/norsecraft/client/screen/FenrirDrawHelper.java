package com.norsecraft.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.norsecraft.client.render.TextureSprite;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

import java.awt.*;

public class FenrirDrawHelper {

    public static final float TEXT_SCALE = 0.7F;

    public static void drawSprite(MatrixStack matrixStack, Identifier texture, int x, int y, TextureSprite sprite, int textureWidth, int textureHeight) {
        setTexture(texture);
        Screen.drawTexture(matrixStack, x, y, (float) sprite.x / 2, (float) sprite.y / 2, sprite.width / 2, sprite.height / 2, textureWidth, textureHeight);
    }

    public static void drawSprite(MatrixStack matrixStack, Identifier texture, int x, int y, TextureSprite sprite) {
        drawSprite(matrixStack, texture, x, y, sprite, 512, 256);
    }

    public static void drawSprite(MatrixStack matrixStack, Identifier texture, int x, int y, TextureSprite sprite, int zOffset, int textureWidth, int textureHeight) {
        setTexture(texture);
        Screen.drawTexture(matrixStack, x, y, zOffset, (float) sprite.x / 2, (float) sprite.y / 2, sprite.width / 2, sprite.height / 2, textureHeight, textureWidth);
    }

    public static void drawSprite(MatrixStack matrixStack, Identifier texture, int x, int y, TextureSprite sprite, int zOffset) {
        drawSprite(matrixStack, texture, x, y, sprite, zOffset, 512, 256);
    }

    public static void setTexture(Identifier texture) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);
    }

    public static void scaleText(MatrixStack matrix) {
        matrix.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);
    }

    public static int getFixedTextCoordinate(int coordinate) {
        return (int) (coordinate * (1F / TEXT_SCALE));
    }

    public static void drawText(MatrixStack matrices, int x, int y, String text, Color color) {
        matrices.push();
        scaleText(matrices);
        MinecraftClient.getInstance().textRenderer.draw(matrices, text, getFixedTextCoordinate(x), getFixedTextCoordinate(y), color.getRGB());
        matrices.pop();
    }

    public static void drawText(MatrixStack matrices, int x, int y, String text) {
        drawText(matrices, x, y, text, Color.WHITE);
    }


}
