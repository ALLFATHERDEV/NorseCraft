package com.norsecraft.client.screen.widget;

import com.norsecraft.client.screen.FenrirDrawHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.awt.*;

public class TextButton extends ButtonWidget {

    private final float scale = 0.7F;
    private final Color color;

    public TextButton(int x, int y, int height, String message, Color color, PressAction onPress) {
        super(x, y, MinecraftClient.getInstance().textRenderer.getWidth(message), height, new LiteralText(message), onPress);
        this.color = color;
    }

    public TextButton(int x, int y, int height, String message, PressAction onPress) {
        this(x, y, height, message, Color.WHITE, onPress);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.getMessage() == null)
            throw new RuntimeException("Could not render text button, message is null");
        FenrirDrawHelper.drawText(matrices, this.x, this.y, this.getMessage().asString(), this.color);
    }
}
