package com.norsecraft.client.screen.widget;

import com.norsecraft.client.screen.FenrirDrawHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.awt.*;

public class TextButton extends ButtonWidget {

    protected final Color color;

    public TextButton(int x, int y, int height, String message, Color color, PressAction onPress) {
        super(x, y, 196, height, FenrirDrawHelper.of(message), onPress);
        this.color = color;
    }

    public TextButton(int x, int y, int height, String message, PressAction onPress) {
        this(x, y, height, message, Color.WHITE, onPress);
    }

    public void setMessage(String message) {
        this.setMessage(FenrirDrawHelper.of(message));
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        FenrirDrawHelper.drawText(matrices, this.x, this.y, this.getMessage().asString(), this.color);
    }


}
