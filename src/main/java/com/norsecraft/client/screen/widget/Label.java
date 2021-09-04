package com.norsecraft.client.screen.widget;

import com.norsecraft.client.screen.FenrirDrawHelper;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class Label implements Drawable {

    private final int x;
    private final int y;
    private final String text;
    private final Color color;

    public Label(int x, int y, String text, Color color) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = color;
    }

    public Label(int x, int y, String text) {
        this(x, y, text, Color.WHITE);
    }


    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        FenrirDrawHelper.drawText(matrices, x, y, text, color);
    }
}
