package com.norsecraft.client.ymir.widget.icon;

import com.norsecraft.client.ymir.YmirScreenDrawing;
import com.norsecraft.client.ymir.widget.data.Texture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class TextureIcon implements Icon{

    private final Texture texture;
    private float opacity = 1f;
    private int color = 0xFF_FFFFFF;

    public TextureIcon(Identifier texture) {
        this(new Texture(texture));
    }

    public TextureIcon(Texture texture) {
        this.texture = texture;
    }


    public float getOpacity() {
        return opacity;
    }

    public TextureIcon setOpacity(float opacity) {
        this.opacity = opacity;
        return this;
    }

    public int getColor() {
        return color;
    }

    public TextureIcon setColor(int color) {
        this.color = color;
        return this;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(MatrixStack matrices, int x, int y, int size) {
        YmirScreenDrawing.texturedRect(matrices, x, y, size, size, texture, color, opacity);
    }
}
