package com.norsecraft.client.ymir;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.norsecraft.client.ymir.screen.YmirScreenImpl;
import com.norsecraft.client.ymir.widget.data.HorizontalAlignment;
import com.norsecraft.client.ymir.widget.data.Texture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public static void drawStringWithShadow(MatrixStack matrices, String s, HorizontalAlignment align, int x, int y, int width, int color) {
        switch (align) {
            case LEFT -> {
                MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, s, x, y, color);
            }

            case CENTER -> {
                int wid = MinecraftClient.getInstance().textRenderer.getWidth(s);
                int l = (width / 2) - (wid / 2);
                MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, s, x + l, y, color);
            }

            case RIGHT -> {
                int wid = MinecraftClient.getInstance().textRenderer.getWidth(s);
                int l = width - wid;
                MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, s, x + l, y, color);
            }
        }
    }


    public static void drawStringWithShadow(MatrixStack matrices, OrderedText text, HorizontalAlignment align, int x, int y, int width, int color) {
        switch (align) {
            case LEFT -> {
                MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, text, x, y, color);
            }

            case CENTER -> {
                int wid = MinecraftClient.getInstance().textRenderer.getWidth(text);
                int l = (width / 2) - (wid / 2);
                MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, text, x + l, y, color);
            }

            case RIGHT -> {
                int wid = MinecraftClient.getInstance().textRenderer.getWidth(text);
                int l = width - wid;
                MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, text, x + l, y, color);
            }
        }
    }


    public static void drawString(MatrixStack matrices, OrderedText s, HorizontalAlignment align, int x, int y, int width, int color) {
        x = (int) (x * (1F / 0.7F));
        y = (int) (y * (1F / 0.7F));

        matrices.push();
        matrices.scale(0.7F, 0.7F, 0.7F);

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
        matrices.pop();
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

    public static int colorAtOpacity(int opaque, float opacity) {
        if (opacity < 0.0f) opacity = 0.0f;
        if (opacity > 1.0f) opacity = 1.0f;

        int a = (int) (opacity * 255.0f);

        return (opaque & 0xFFFFFF) | (a << 24);
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

    public static void renderTooltip(MatrixStack matrices, ItemStack stack, int x, int y, int width, int height) {
        renderTooltip(matrices, getTooltipFromItem(stack), stack.getTooltipData(), x, y, width, height);
    }

    public static void renderTooltip(MatrixStack matrices, List<Text> lines, Optional<TooltipData> data, int x, int y, int width, int height) {
        List<TooltipComponent> list = (List)lines.stream().map(Text::asOrderedText).map(TooltipComponent::of).collect(Collectors.toList());
        data.ifPresent((datax) -> {
            list.add(1, TooltipComponent.of(datax));
        });
        renderTooltipFromComponents(matrices, list, x, y, width, height);
    }

    public static List<Text> getTooltipFromItem(ItemStack stack) {
        return stack.getTooltip(MinecraftClient.getInstance().player, MinecraftClient.getInstance().options.advancedItemTooltips ? TooltipContext.Default.ADVANCED : TooltipContext.Default.NORMAL);
    }

    public static void renderTooltip(MatrixStack matrices, Text text, int x, int y, int width, int height) {
        renderOrderedTooltip(matrices, Arrays.asList(text.asOrderedText()), x, y, width, height);
    }

    public static void renderTooltip(MatrixStack matrices, List<Text> lines, int x, int y, int width, int height) {
        renderOrderedTooltip(matrices, Lists.transform(lines, Text::asOrderedText), x, y, width, height);
    }

    public static void renderOrderedTooltip(MatrixStack matrices, List<? extends OrderedText> lines, int x, int y, int width, int height) {
        renderTooltipFromComponents(matrices, (List)lines.stream().map(TooltipComponent::of).collect(Collectors.toList()), x, y, width, height);
    }

    public static void renderTooltipFromComponents(MatrixStack matrices, List<TooltipComponent> components, int x, int y, int width, int height) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        if (!components.isEmpty()) {
            int i = 0;
            int j = components.size() == 1 ? -2 : 0;

            TooltipComponent tooltipComponent;
            for(Iterator var7 = components.iterator(); var7.hasNext(); j += tooltipComponent.getHeight()) {
                tooltipComponent = (TooltipComponent)var7.next();
                int k = tooltipComponent.getWidth(textRenderer);
                if (k > i) {
                    i = k;
                }
            }

            int l = x + 12;
            int m = y - 12;
            if (l + i > width) {
                l -= 28 + i;
            }

            if (m + j + 6 > height) {
                m = height - j - 6;
            }

            matrices.push();
            int p = -267386864;
            int q = 1347420415;
            int r = 1344798847;
            float f = itemRenderer.zOffset;
            itemRenderer.zOffset = 400.0F;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            Matrix4f matrix4f = matrices.peek().getModel();
            fillGradient(matrix4f, bufferBuilder, l - 3, m - 4, l + i + 3, m - 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, l - 3, m + j + 3, l + i + 3, m + j + 4, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, l - 3, m - 3, l + i + 3, m + j + 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, l - 4, m - 3, l - 3, m + j + 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, l + i + 3, m - 3, l + i + 4, m + j + 3, 400, -267386864, -267386864);
            fillGradient(matrix4f, bufferBuilder, l - 3, m - 3 + 1, l - 3 + 1, m + j + 3 - 1, 400, 1347420415, 1344798847);
            fillGradient(matrix4f, bufferBuilder, l + i + 2, m - 3 + 1, l + i + 3, m + j + 3 - 1, 400, 1347420415, 1344798847);
            fillGradient(matrix4f, bufferBuilder, l - 3, m - 3, l + i + 3, m - 3 + 1, 400, 1347420415, 1347420415);
            fillGradient(matrix4f, bufferBuilder, l - 3, m + j + 2, l + i + 3, m + j + 3, 400, 1344798847, 1344798847);
            RenderSystem.enableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            bufferBuilder.end();
            BufferRenderer.draw(bufferBuilder);
            RenderSystem.disableBlend();
            RenderSystem.enableTexture();
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            matrices.translate(0.0D, 0.0D, 400.0D);
            int t = m;

            int v;
            TooltipComponent tooltipComponent3;
            for(v = 0; v < components.size(); ++v) {
                tooltipComponent3 = (TooltipComponent)components.get(v);
                tooltipComponent3.drawText(textRenderer, l, t, matrix4f, immediate);
                t += tooltipComponent3.getHeight() + (v == 0 ? 2 : 0);
            }

            immediate.draw();
            matrices.pop();
            t = m;

            for(v = 0; v < components.size(); ++v) {
                tooltipComponent3 = (TooltipComponent)components.get(v);
                tooltipComponent3.drawItems(textRenderer, l, t, matrices, itemRenderer, 400, MinecraftClient.getInstance().getTextureManager());
                t += tooltipComponent3.getHeight() + (v == 0 ? 2 : 0);
            }

            itemRenderer.zOffset = f;
        }
    }

    public static void fillGradient(MatrixStack matrices, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, int z) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        fillGradient(matrices.peek().getModel(), bufferBuilder, startX, startY, endX, endY, z, colorStart, colorEnd);
        tessellator.draw();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    public static void fillGradient(Matrix4f matrix, BufferBuilder bufferBuilder, int startX, int startY, int endX, int endY, int z, int colorStart, int colorEnd) {
        float f = (float)(colorStart >> 24 & 255) / 255.0F;
        float g = (float)(colorStart >> 16 & 255) / 255.0F;
        float h = (float)(colorStart >> 8 & 255) / 255.0F;
        float i = (float)(colorStart & 255) / 255.0F;
        float j = (float)(colorEnd >> 24 & 255) / 255.0F;
        float k = (float)(colorEnd >> 16 & 255) / 255.0F;
        float l = (float)(colorEnd >> 8 & 255) / 255.0F;
        float m = (float)(colorEnd & 255) / 255.0F;
        bufferBuilder.vertex(matrix, (float)endX, (float)startY, (float)z).color(g, h, i, f).next();
        bufferBuilder.vertex(matrix, (float)startX, (float)startY, (float)z).color(g, h, i, f).next();
        bufferBuilder.vertex(matrix, (float)startX, (float)endY, (float)z).color(k, l, m, j).next();
        bufferBuilder.vertex(matrix, (float)endX, (float)endY, (float)z).color(k, l, m, j).next();
    }

}
