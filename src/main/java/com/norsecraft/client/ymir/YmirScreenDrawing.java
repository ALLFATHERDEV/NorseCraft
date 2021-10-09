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

/**
 * This class is a helper class for some gui drawings
 */
public class YmirScreenDrawing {


    /**
     * Draw a textured rect
     * It is recommend to use the texturedGuiRect method
     *
     * @param matrices the render matrix from mc
     * @param x        the x position where it starts to draw
     * @param y        the y position where it starts to draw
     * @param width    the width from the rect
     * @param height   the height from the rect
     * @param texture  the texture file location
     * @param u1       the top left x position of the sprite
     * @param v1       the top left y position of the sprite
     * @param u2       the bottom right x position of the sprite
     * @param v2       the bottom right y position of the sprite
     * @param color    some extra color
     */
    public static void texturedRect(MatrixStack matrices, int x, int y, int width, int height, Identifier texture, float u1, float v1, float u2, float v2, int color) {
        texturedRect(matrices, x, y, width, height, texture, u1, v1, u2, v2, color, 1.0f);
    }

    /**
     * Draw a textured rect
     * It is recommend to use the texturedGuiRect method
     *
     * @param matrices the render matrix from mc
     * @param x        the x position where it starts to draw
     * @param y        the y position where it starts to draw
     * @param width    the width from the rect
     * @param height   the height from the rect
     * @param texture  the texture object
     * @param color    some extra color
     */
    public static void texturedRect(MatrixStack matrices, int x, int y, int width, int height, Texture texture, int color) {
        texturedRect(matrices, x, y, width, height, texture, color, 1.0f);
    }

    /**
     * Draw a textured rect
     * It is recommend to use the texturedGuiRect method
     *
     * @param matrices the render matrix from mc
     * @param x        the x position where it starts to draw
     * @param y        the y position where it starts to draw
     * @param width    the width from the rect
     * @param height   the height from the rect
     * @param texture  the texture object
     * @param color    some extra color
     * @param opacity  the opacity of the texture
     */
    public static void texturedRect(MatrixStack matrices, int x, int y, int width, int height, Texture texture, int color, float opacity) {
        texturedRect(matrices, x, y, width, height, texture.image(), texture.u1(), texture.v1(), texture.u2(), texture.v2(), color, opacity);
    }

    /**
     * The actual render method where it get rendered
     * This and the methods above are for default minecraft textures not our textures, bcs our textures are double sized or tripple sized
     *
     * @param matrices the render matrix from mc
     * @param x        the x position where it starts to draw
     * @param y        the y position where it starts to draw
     * @param width    the width from the rect
     * @param height   the height from the rect
     * @param texture  the image file location
     * @param u1       the top left x position of the sprite
     * @param v1       the top left y position of the sprite
     * @param u2       the bottom right x position of the sprite
     * @param v2       the bottom right y position of the sprite
     * @param color    some extra color
     * @param opacity  the opacity of the sprite
     */
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

    /**
     * This method is explicit for our images that are in the folder "textures/gui"
     *
     * @param matrixStack the render matrix from mc
     * @param x           the x position where it starts to draw
     * @param y           the y position where it starts to draw
     * @param texture     the texture object that holds all the texture sprite data
     */
    public static void texturedGuiRect(MatrixStack matrixStack, int x, int y, Texture texture) {
        texturedGuiRect(matrixStack, x, y, (int) texture.u2(), (int) texture.v2(), texture);
    }

    /**
     * This method is rendering our sprites. It used the "texturedRect" method but with fixed pixel coordinates
     *
     * @param matrixStack the render matrix from mc
     * @param x           the x position where it starts to draw
     * @param y           the y position where it starts to draw
     * @param width       the width of the sprite
     * @param height      the height of the sprite
     * @param texture     the texture object that holds all the texture sprite data
     */
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

    /**
     * Simple colored rect with normal color
     *
     * @param matrices the render matrix from mc
     * @param left     the x position
     * @param top      the y position
     * @param width    the width of the rect
     * @param height   the height of the rect
     * @param color    the color of the rect
     */
    public static void coloredRect(MatrixStack matrices, int left, int top, int width, int height, int color) {
        if (width <= 0) width = 1;
        if (height <= 0) height = 1;

        DrawableHelper.fill(matrices, left, top, left + width, top + height, color);
    }

    /**
     * This draws the scrollbar
     *
     * @param matrices    the render matrix from mc
     * @param x           the x position
     * @param y           the y position
     * @param width       the width of the scrollbar
     * @param height      the height of the scrollbar
     * @param topleft     the top left color
     * @param panel       the panel color
     * @param bottomright the shadow color
     */
    public static void drawBeveledPanel(MatrixStack matrices, int x, int y, int width, int height, int topleft, int panel, int bottomright) {
        coloredRect(matrices, x, y, width, height, panel); //Center panel
        coloredRect(matrices, x, y, width - 1, 1, topleft); //Top shadow
        coloredRect(matrices, x, y + 1, 1, height - 2, topleft); //Left shadow
        coloredRect(matrices, x + width - 1, y + 1, 1, height - 1, bottomright); //Right hilight
        coloredRect(matrices, x + 1, y + height - 1, width - 1, 1, bottomright); //Bottom hilight
    }


    /**
     * Draws a string with horizontal alignment
     * This method also scales the matrix down, from 1.0 to 0.7.
     * This has the effect that the text is smaller and fits in every texture from us
     *
     * @param matrices the render matrix from mc
     * @param s        the text to render
     * @param align    the alignment
     * @param x        the x position
     * @param y        the y position
     * @param width    set a max width
     * @param color    set the color
     */
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

    /**
     * Same method as above with the different it uses a string as text
     *
     * @param matrices the render matrix from mc
     * @param s        the text to render
     * @param align    the alignment
     * @param x        the x position
     * @param y        the y position
     * @param width    set a max width
     * @param color    set the color
     */
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

    /**
     * Draws a hovered text
     *
     * @param matrices  the render matrix from mc
     * @param textStyle set a extra text style of the text. Can be null
     * @param x         the x position
     * @param y         the y position
     */
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


    public static Text of(String msg) {
        return new LiteralText(msg);
    }


}
