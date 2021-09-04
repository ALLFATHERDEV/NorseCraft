package com.norsecraft.client.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.norsecraft.client.render.TextureSprite;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ImageButton extends ButtonWidget {

    protected final Identifier texture;
    protected final TextureSprite sprite;
    protected final TextureSprite hoverSprite;

    public ImageButton(int x, int y, int width, int height, Text message, Identifier texture, TextureSprite sprite, TextureSprite hoverSprite, PressAction onPress) {
        super(x, y, width, height, message, onPress);
        this.texture = texture;
        this.sprite = sprite;
        this.hoverSprite = hoverSprite;
    }

    public boolean shouldCustomRender() {
        return false;
    }

    public void renderCustom(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {

    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        if(this.shouldCustomRender()) {
            this.renderCustom(matrices, mouseX, mouseY, delta);
        } else {
            if (isHovered() && this.hoverSprite != null) {
                this.drawTexture(matrices, this.x, this.y, hoverSprite.x, hoverSprite.y, hoverSprite.width, hoverSprite.height);
            } else {
                this.drawTexture(matrices, this.x, this.y, sprite.x, sprite.y, sprite.width, sprite.height);
            }
            this.drawTexture(matrices, this.x + width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        }
        this.renderBackground(matrices, client, mouseX, mouseY);
    }
}
