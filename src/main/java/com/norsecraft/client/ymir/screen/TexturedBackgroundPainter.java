package com.norsecraft.client.ymir.screen;

import com.norsecraft.client.ymir.YmirScreenDrawing;
import com.norsecraft.client.ymir.widget.YmirWidget;
import com.norsecraft.client.ymir.widget.data.Texture;
import net.minecraft.client.util.math.MatrixStack;

public class TexturedBackgroundPainter implements BackgroundPainter {

    private final Texture texture;

    public TexturedBackgroundPainter(Texture texture) {
        this.texture = texture;
    }

    @Override
    public Texture getTexture() {
        return this.texture;
    }

    @Override
    public void paintBackground(MatrixStack matrices, int left, int top, YmirWidget panel) {
        YmirScreenDrawing.texturedGuiRect(matrices, left, top, texture);
    }
}
