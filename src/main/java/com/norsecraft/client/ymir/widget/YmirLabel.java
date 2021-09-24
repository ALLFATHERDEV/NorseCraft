package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.YmirScreenDrawing;
import com.norsecraft.client.ymir.widget.data.HorizontalAlignment;
import com.norsecraft.client.ymir.widget.data.InputResult;
import com.norsecraft.client.ymir.widget.data.VerticalAlignment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class YmirLabel extends YmirWidget {

    protected Text text;
    protected HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
    protected VerticalAlignment verticalAlignment = VerticalAlignment.TOP;
    protected int color;

    public static final int DEFAULT_TEXT_COLOR = 0x404040;

    public YmirLabel(String text, int color) {
        this(new LiteralText(text), color);
    }


    public YmirLabel(Text text, int color) {
        this.text = text;
        this.color = color;
    }

    public YmirLabel(String text) {
        this(text, DEFAULT_TEXT_COLOR);
    }


    public YmirLabel(Text text) {
        this(text, DEFAULT_TEXT_COLOR);
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        MinecraftClient mc = MinecraftClient.getInstance();
        TextRenderer renderer = mc.textRenderer;
        int yOffset = switch (verticalAlignment) {
            case CENTER -> height / 2 - renderer.fontHeight / 2;
            case BOTTOM -> height - renderer.fontHeight;
            case TOP -> 0;
        };

        YmirScreenDrawing.drawString(matrices, text.asOrderedText(), horizontalAlignment, x, y + yOffset, this.getWidth(), color);

        Style hoveredTextStyle = getTextStyleAt(mouseX, mouseY);
        YmirScreenDrawing.drawTextHover(matrices, hoveredTextStyle, x + mouseX , y + mouseY);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public InputResult onClick(int x, int y, int button) {
        Style hoveredStyle = getTextStyleAt(x, y);
        if(hoveredStyle != null) {
            Screen screen = MinecraftClient.getInstance().currentScreen;
            if(screen != null) {
                return InputResult.of(screen.handleTextClick(hoveredStyle));
            }
        }
        return InputResult.IGNORED;
    }

    @Environment(EnvType.CLIENT)
    @Nullable
    public Style getTextStyleAt(int x, int y) {
        if(isWithinBounds(x, y))
            return MinecraftClient.getInstance().textRenderer.getTextHandler().getStyleAt(text, x);
        return null;
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(x, Math.max(8, y));
    }

    public int getColor() {
        return color;
    }

    public YmirLabel setColor(int color) {
        this.color = color;
        return this;
    }

    public Text getText() {
        return text;
    }

    public YmirLabel setText(Text text) {
        this.text = text;
        return this;
    }

    public HorizontalAlignment getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public YmirLabel setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
        return this;
    }

    public VerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }

    public YmirLabel setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
        return this;
    }

    @Override
    public void addNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, text);
    }
}
