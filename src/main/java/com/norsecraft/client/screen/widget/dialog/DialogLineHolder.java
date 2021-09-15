package com.norsecraft.client.screen.widget.dialog;

import com.norsecraft.client.screen.FenrirDrawHelper;
import com.norsecraft.client.screen.widget.WidgetBounds;
import com.norsecraft.common.util.CheckUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import java.awt.*;

public class DialogLineHolder {

    private final WidgetBounds bounds;
    private String text;
    private Color color;
    private boolean visible = true;

    public DialogLineHolder(WidgetBounds bounds) {
        CheckUtil.notNull(bounds, "Could not create dialog line holder, bounds are null");
        this.bounds = bounds;
    }

    public boolean isEmpty() {
        return this.text == null;
    }

    public void setY(int y) {
        this.bounds.y = y;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public WidgetBounds getBounds() {
        return bounds;
    }

    public int getLineCount() {
        CheckUtil.notNull(text, "Could not calculate height, text is null");
        int width = MinecraftClient.getInstance().textRenderer.getWidth(text);
        return Math.abs(width / bounds.width);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void render(MatrixStack matrices) {
        if (!isEmpty() && visible) {
            if (color == null)
                color = Color.WHITE;
            FenrirDrawHelper.drawTextWithLength(matrices, bounds.x, bounds.y, FenrirDrawHelper.of(this.text), bounds.width, 6, this.color);
        }
    }

}
