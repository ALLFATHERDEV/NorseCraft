package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.YmirScreenDrawing;
import com.norsecraft.client.ymir.widget.data.HorizontalAlignment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;

import java.util.function.Supplier;

public class YmirDynamicLabel extends YmirWidget {

    protected Supplier<String> text;
    protected HorizontalAlignment alignment = HorizontalAlignment.LEFT;
    protected int color;

    public static final int DEFAULT_TEXT_COLOR = 0x404040;

    public YmirDynamicLabel(Supplier<String> text, int color) {
        this.text = text;
        this.color = color;
    }

    public YmirDynamicLabel(Supplier<String> text) {
        this(text, DEFAULT_TEXT_COLOR);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        String tr = text.get();
        YmirScreenDrawing.drawString(matrices, tr, alignment, x, y, this.getWidth(), color);
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(x, 20);
    }

    public YmirDynamicLabel setColor(int color) {
        this.color = color;
        return this;
    }

    public YmirDynamicLabel setText(Supplier<String> text) {
        this.text = text;
        return this;
    }

    public YmirDynamicLabel setAlignment(HorizontalAlignment align) {
        this.alignment = align;
        return this;
    }

}
