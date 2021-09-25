package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.YmirScreenDrawing;
import com.norsecraft.client.ymir.widget.data.HorizontalAlignment;
import com.norsecraft.client.ymir.widget.data.InputResult;
import com.norsecraft.client.ymir.widget.data.VerticalAlignment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * multiline  label widget
 */
public class YmirText extends YmirWidget {

    private Text text;
    private int color;
    protected HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
    protected VerticalAlignment verticalAlignment = VerticalAlignment.TOP;
    @Environment(EnvType.CLIENT)
    private List<OrderedText> wrappedLines;
    private boolean wrappingScheduled = false;

    public YmirText(Text text) {
        this(text, YmirLabel.DEFAULT_TEXT_COLOR);
    }

    public YmirText(Text text, int color) {
        this.text = Objects.requireNonNull(text, "text must not be null");
        this.color = color;
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(x, y);
        wrappingScheduled = true;
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Environment(EnvType.CLIENT)
    private void wrapLines() {
        TextRenderer font = MinecraftClient.getInstance().textRenderer;
        wrappedLines = font.wrapLines(text, (int) ((width * (1F / 0.7F))));
    }

    @Environment(EnvType.CLIENT)
    @Nullable
    public Style getTextStyleAt(int x, int y) {
        TextRenderer font = MinecraftClient.getInstance().textRenderer;
        int lineIndex = y / font.fontHeight;

        if (lineIndex >= 0 && lineIndex < wrappedLines.size()) {
            OrderedText line = wrappedLines.get(lineIndex);
            return font.getTextHandler().getStyleAt(line, x);
        }
        return null;
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        if (wrappedLines == null || wrappingScheduled) {
            wrapLines();
            wrappingScheduled = false;
        }

        TextRenderer font = MinecraftClient.getInstance().textRenderer;

        int yOffset = switch (verticalAlignment) {
            case CENTER -> height / 2 - font.fontHeight * wrappedLines.size() / 2;
            case BOTTOM -> height - font.fontHeight * wrappedLines.size();
            case TOP -> 0;
        };

        for (int i = 0; i < wrappedLines.size(); i++) {
            OrderedText line = wrappedLines.get(i);
            YmirScreenDrawing.drawString(matrices, line, horizontalAlignment, x, y + yOffset + i * font.fontHeight, width, color);
        }

        Style hoveredTextStyle = getTextStyleAt(mouseX, mouseY);
        YmirScreenDrawing.drawTextHover(matrices, hoveredTextStyle, x + mouseX, y + mouseY);
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        if (button != 0) return InputResult.IGNORED;

        Style hoveredStyle = getTextStyleAt(x, y);
        if (hoveredStyle != null) {
            boolean processed = MinecraftClient.getInstance().currentScreen.handleTextClick(hoveredStyle);
            return InputResult.of(processed);
        }
        return InputResult.IGNORED;
    }

    public Text getText() {
        return text;
    }

    public YmirText setText(Text text) {
        Objects.requireNonNull(text, "text is null");
        this.text = text;
        wrappingScheduled = true;

        return this;
    }

    public int getColor() {
        return color;
    }

    public YmirText setColor(int color) {
        this.color = color;
        return this;
    }


    public HorizontalAlignment getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public YmirText setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
        return this;
    }


    public VerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }

    public YmirText setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
        return this;
    }
}
