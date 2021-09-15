package com.norsecraft.client.screen.widget;

import com.norsecraft.client.screen.FenrirDrawHelper;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.awt.*;

public class Label implements Drawable {

    private final int x;
    private final int y;
    private String text;
    private Color color;
    private int maxWidth;
    private boolean visible;

    public Label(int x, int y, int maxWidth, String text, Color color) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = color;
        this.maxWidth = maxWidth;
        this.visible = true;
    }

    public Label(int x, int y, String text) {
        this(x, y, 512, text, Color.WHITE);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (visible)
            FenrirDrawHelper.drawTextWithLength(matrices, x, y, new LiteralText(text), maxWidth, 5, color);
    }
}
