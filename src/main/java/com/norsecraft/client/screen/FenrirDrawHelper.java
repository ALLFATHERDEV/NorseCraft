package com.norsecraft.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.render.TextureSprite;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;

public class FenrirDrawHelper {

    public static final int TEXT_HEIGHT = 9;
    public static final float TEXT_SCALE = 0.7F;
    public static final ButtonWidget.PressAction EMPTY_ACTION = (button) -> {
        NorseCraftMod.LOGGER.info("Empty button clicked");
    };

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

    public static void drawTextWithLength(MatrixStack matrices, int x, int y, Text text, int maxWidth, int yOffset, Color color) {
        matrices.push();
        scaleText(matrices);
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        int textWidth = renderer.getWidth(text);
        String msg = text.asString();
        if (textWidth > maxWidth) {
            int width = 0;
            String prevLine;
            int yCounter = 0;
            String line = null;
            while (width < textWidth) {
                int length = renderer.getTextHandler().getTrimmedLength(line == null ? msg : line, maxWidth, Style.EMPTY);
                String tmp = renderer.trimToWidth(line == null ? msg : line, maxWidth);
                prevLine = line == null ? msg : line;
                line = tmp;
                renderer.draw(matrices, line, getFixedTextCoordinate(x), getFixedTextCoordinate(y + yCounter * yOffset), color.getRGB());
                line = prevLine.substring(length);
                yCounter++;
                width += maxWidth;
            }
        } else {
            renderer.draw(matrices, msg, getFixedTextCoordinate(x), getFixedTextCoordinate(y), color.getRGB());
        }
        matrices.pop();
    }

    public static Text of(String msg) {
        return new LiteralText(msg);
    }

    public static Text empty() {
        return of("");
    }

}
