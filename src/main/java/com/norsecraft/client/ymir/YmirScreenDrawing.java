package com.norsecraft.client.ymir;

import com.mojang.blaze3d.systems.RenderSystem;
import com.norsecraft.client.ymir.screen.YmirScreenImpl;
import com.norsecraft.client.ymir.widget.data.HorizontalAlignment;
import com.norsecraft.client.ymir.widget.data.Texture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;

public class YmirScreenDrawing {

    public static final Text EMPTY_TEXT = of("");

    public static void texturedRect(MatrixStack matrices, int x, int y, int width, int height, Identifier texture, int color) {
        texturedRect(matrices, x, y, width, height, texture, 0, 0, 1, 1, color, 1.0f);
    }


    public static void texturedRect(MatrixStack matrices, int x, int y, int width, int height, Identifier texture, int color, float opacity) {
        texturedRect(matrices, x, y, width, height, texture, 0, 0, 1, 1, color, opacity);
    }


    public static void texturedRect(MatrixStack matrices, int x, int y, int width, int height, Identifier texture, float u1, float v1, float u2, float v2, int color) {
        texturedRect(matrices, x, y, width, height, texture, u1, v1, u2, v2, color, 1.0f);
    }

    public static void texturedRect(MatrixStack matrices, int x, int y, int width, int height, Texture texture, int color) {
        texturedRect(matrices, x, y, width, height, texture, color, 1.0f);
    }


    public static void texturedRect(MatrixStack matrices, int x, int y, int width, int height, Texture texture, int color, float opacity) {
        texturedRect(matrices, x, y, width, height, texture.image(), texture.u1(), texture.v1(), texture.u2(), texture.v2(), color, opacity);
    }

    public static void texturedRect(MatrixStack matrices, int x, int y, int width, int height, Identifier texture, float u1, float v1, float u2, float v2, int color, float opacity) {
        if (width <= 0) width = 1;
        if (height <= 0) height = 1;

        x = x * 2;
        y = y * 2;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        matrices.push();
        matrices.scale(0.5F, 0.5F, 0.5F);
        Matrix4f model = matrices.peek().getModel();
        matrices.pop();
        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(r, g, b, opacity);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(model, x, y + height, 0).texture(u1, v2).next();
        buffer.vertex(model, x + width, y + height, 0).texture(u2, v2).next();
        buffer.vertex(model, x + width, y, 0).texture(u2, v1).next();
        buffer.vertex(model, x, y, 0).texture(u1, v1).next();
        buffer.end();
        BufferRenderer.draw(buffer);
        RenderSystem.disableBlend();
    }

    public static void texturedGuiRect(MatrixStack matrixStack, int x, int y, Texture texture) {
        texturedGuiRect(matrixStack, x, y, (int) texture.u2(), (int) texture.v2(), texture);
    }

    public static void texturedGuiRect(MatrixStack matrixStack, int x, int y, int width, int height, Texture texture) {
        float px = 1 / texture.textureWidth();
        float py = 1 / texture.textureHeight();
        texturedRect(matrixStack,
                x,
                y,
                width,
                height,
                texture.image(),
                texture.u1() * px,
                texture.v1() * py,
                (texture.u1() + texture.u2()) * px,
                (texture.v1() + texture.v2()) * py,
                0xFFFFFF);
    }


    public static void coloredRect(MatrixStack matrices, int left, int top, int width, int height, int color) {
        if (width <= 0) width = 1;
        if (height <= 0) height = 1;

        DrawableHelper.fill(matrices, left, top, left + width, top + height, color);
    }

    public static void drawBeveledPanel(MatrixStack matrices, int x, int y) {
        drawBeveledPanel(matrices, x, y, 18, 18, 0xFF373737, 0xFF8b8b8b, 0xFFFFFFFF);
    }

    public static void drawBeveledPanel(MatrixStack matrices, int x, int y, int width, int height) {
        drawBeveledPanel(matrices, x, y, width, height, 0xFF373737, 0xFF8b8b8b, 0xFFFFFFFF);
    }


    public static void drawBeveledPanel(MatrixStack matrices, int x, int y, int width, int height, int topleft, int panel, int bottomright) {
        coloredRect(matrices, x, y, width, height, panel); //Center panel
        coloredRect(matrices, x, y, width - 1, 1, topleft); //Top shadow
        coloredRect(matrices, x, y + 1, 1, height - 2, topleft); //Left shadow
        coloredRect(matrices, x + width - 1, y + 1, 1, height - 1, bottomright); //Right hilight
        coloredRect(matrices, x + 1, y + height - 1, width - 1, 1, bottomright); //Bottom hilight
    }

    public static void drawString(MatrixStack matrices, OrderedText s, HorizontalAlignment align, int x, int y, int width, int color) {
        switch (align) {
            case LEFT -> {
                MinecraftClient.getInstance().textRenderer.draw(matrices, s, x, y, color);
            }

            case CENTER -> {
                int wid = MinecraftClient.getInstance().textRenderer.getWidth(s);
                int l = (width / 2) - (wid / 2);
                MinecraftClient.getInstance().textRenderer.draw(matrices, s, x + l, y, color);
            }

            case RIGHT -> {
                int wid = MinecraftClient.getInstance().textRenderer.getWidth(s);
                int l = width - wid;
                MinecraftClient.getInstance().textRenderer.draw(matrices, s, x + l, y, color);
            }
        }
    }

    public static void drawString(MatrixStack matrices, String s, HorizontalAlignment align, int x, int y, int width, int color) {
        switch (align) {
            case LEFT -> {
                MinecraftClient.getInstance().textRenderer.draw(matrices, s, x, y, color);
            }

            case CENTER -> {
                int wid = MinecraftClient.getInstance().textRenderer.getWidth(s);
                int l = (width / 2) - (wid / 2);
                MinecraftClient.getInstance().textRenderer.draw(matrices, s, x + l, y, color);
            }

            case RIGHT -> {
                int wid = MinecraftClient.getInstance().textRenderer.getWidth(s);
                int l = width - wid;
                MinecraftClient.getInstance().textRenderer.draw(matrices, s, x + l, y, color);
            }
        }
    }

    public static void drawTextHover(MatrixStack matrices, @Nullable Style textStyle, int x, int y) {
        if (MinecraftClient.getInstance().currentScreen instanceof YmirScreenImpl screen) {
            screen.renderTextHover(matrices, textStyle, x, y);
        }
    }

    public static int multiplyColor(int color, float amount) {
        int a = color & 0xFF000000;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;

        r = Math.min(r * amount, 1.0f);
        g = Math.min(g * amount, 1.0f);
        b = Math.min(b * amount, 1.0f);

        int ir = (int) (r * 255);
        int ig = (int) (g * 255);
        int ib = (int) (b * 255);

        return
                a |
                        (ir << 16) |
                        (ig << 8) |
                        ib;
    }

    public static Text of(String msg) {
        return new LiteralText(msg);
    }

}
