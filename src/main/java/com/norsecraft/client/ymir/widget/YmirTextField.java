package com.norsecraft.client.ymir.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.norsecraft.client.ymir.YmirScreenDrawing;
import com.norsecraft.client.ymir.screen.BackgroundPainter;
import com.norsecraft.client.ymir.widget.data.InputResult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class YmirTextField extends YmirWidget {

    public static final int OFFSET_X_TEXT = 4;

    @Environment(EnvType.CLIENT)
    private TextRenderer font;

    private String text = "";
    private int maxLength = 16;
    private boolean editable = true;


    private int enabledColor = 0xE0E0E0;
    private int uneditableColor = 0x707070;

    @Nullable
    private Text suggestion = null;
    private int cursor = 0;
    private int select = -1;
    private Consumer<String> onChanged;
    private Predicate<String> textPredicate;

    @Environment(EnvType.CLIENT)
    @Nullable
    private BackgroundPainter backgroundPainter;

    public YmirTextField() {

    }

    public YmirTextField(@Nullable Text suggestion) {
        this.suggestion = suggestion;
    }

    public void setText(String s) {
        if (this.textPredicate == null || this.textPredicate.test(s)) {
            this.text = (s.length() > maxLength) ? s.substring(0, maxLength) : s;
            if (onChanged != null)
                onChanged.accept(this.text);
        }
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(x, 20);
    }

    public void setCursorPos(int location) {
        this.cursor = MathHelper.clamp(location, 0, this.text.length());
    }

    public int getMaxLength() {
        return this.maxLength;
    }

    public int getCursor() {
        return cursor;
    }

    @Nullable
    public String getSelection() {
        if (select < 0) return null;
        if (select == cursor) return null;

        if (select > text.length())
            select = text.length();
        if (cursor < 0)
            cursor = 0;
        if (cursor > text.length())
            cursor = text.length();

        int start = Math.min(select, cursor);
        int end = Math.max(select, cursor);

        return text.substring(start, end);
    }

    public boolean isEditable() {
        return editable;
    }

    @Environment(EnvType.CLIENT)
    protected void renderTextField(MatrixStack matrices, int x, int y) {
        if (this.font == null) this.font = MinecraftClient.getInstance().textRenderer;

        int borderColor = this.isFocused() ? 0xFF_FFFFA0 : 0xFF_A0A0A0;
        YmirScreenDrawing.coloredRect(matrices, x - 1, y - 1, width + 2, height + 2, borderColor);
        YmirScreenDrawing.coloredRect(matrices, x, y, width, height, 0xFF000000);

        int textColor = this.editable ? this.enabledColor : this.uneditableColor;

        String trimText = font.trimToWidth(this.text, this.width - OFFSET_X_TEXT);

        boolean selection = select != -1;
        boolean focused = this.isFocused();

        int textX = x + OFFSET_X_TEXT;
        int textY = y + (height - 8) / 2;

        int adjustedCursor = this.cursor;
        if (adjustedCursor > trimText.length())
            adjustedCursor = trimText.length();

        int preCursorAdvance = textX;
        if (!trimText.isEmpty()) {
            String string_2 = trimText.substring(0, adjustedCursor);
            preCursorAdvance = font.drawWithShadow(matrices, string_2, textX, textY, textColor);
        }

        if (adjustedCursor < trimText.length())
            font.drawWithShadow(matrices, trimText.substring(adjustedCursor), preCursorAdvance - 1, (float) textY, textColor);

        if (text.length() == 0 && this.suggestion != null)
            font.drawWithShadow(matrices, this.suggestion, textX, textY, 0xFF808080);

        if (focused && !selection) {
            if (adjustedCursor < trimText.length()) {
                YmirScreenDrawing.coloredRect(matrices, preCursorAdvance - 1, textY - 2, 1, 12, 0xFFD0D0D0);
            } else {
                font.drawWithShadow(matrices, "_", preCursorAdvance, textY, textColor);
            }
        }

        if (selection) {
            int a = getCaretOffset(text, cursor);
            int b = getCaretOffset(text, select);
            if (b < a) {
                int tmp = b;
                b = a;
                a = tmp;
            }
            invertedRect(matrices, textX + a - 1, textY - 1, Math.min(b - a, width - OFFSET_X_TEXT), 12);
        }
    }

    @Environment(EnvType.CLIENT)
    private void invertedRect(MatrixStack matrices, int x, int y, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        Matrix4f model = matrices.peek().getModel();
        RenderSystem.setShaderColor(0.0F, 0.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.disableTexture();
        RenderSystem.enableColorLogicOp();
        RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        buffer.vertex(model, x, y + height, 0).next();
        buffer.vertex(model, x + width, y + height, 0).next();
        buffer.vertex(model, x + width, y, 0).next();
        buffer.vertex(model, x, y, 0).next();
        buffer.end();
        BufferRenderer.draw(buffer);
        RenderSystem.disableColorLogicOp();
        RenderSystem.enableTexture();
    }

    public YmirTextField setTextPredicate(Predicate<String> textPredicate) {
        this.textPredicate = textPredicate;
        return this;
    }

    public YmirTextField setChangedListener(Consumer<String> listener) {
        this.onChanged = listener;
        return this;
    }

    public YmirTextField setMaxLength(int max) {
        this.maxLength = max;
        if (this.text.length() > max) {
            this.text = this.text.substring(0, max);
            if (onChanged != null)
                this.onChanged.accept(this.text);
        }
        return this;
    }

    public YmirTextField setEnabledColor(int color) {
        this.enabledColor = color;
        return this;
    }

    public YmirTextField setDisabledColor(int color) {
        this.uneditableColor = color;
        return this;
    }

    public YmirTextField setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }

    @Nullable
    public Text getSuggestion() {
        return suggestion;
    }

    public YmirTextField setSuggestion(@Nullable String suggestion) {
        this.suggestion = suggestion != null ? new LiteralText(suggestion) : null;
        return this;
    }

    public YmirTextField setSuggestion(@Nullable  Text suggestion) {
        this.suggestion = suggestion;
        return this;
    }

    @Environment(EnvType.CLIENT)
    public void setBackgroundPainter(BackgroundPainter backgroundPainter) {
        this.backgroundPainter = backgroundPainter;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    @Override
    public void onFocusGained() {

    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        renderTextField(matrices, x, y);
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        requestFocus();
        cursor = getCaretPos(this.text, x - OFFSET_X_TEXT);
        return InputResult.PROCESSED;
    }

    @Override
    public void onCharTyped(char ch) {
        if(this.text.length() < this.maxLength) {
            if(cursor < 0) cursor = 0;
            if(cursor > this.text.length()) cursor = this.text.length();

            String before = this.text.substring(0, cursor);
            String after = this.text.substring(cursor);
            this.text = before + ch + after;
            cursor++;
            if(onChanged != null)
                onChanged.accept(text);
        }
    }

    @Override
    public void onKeyPressed(int ch, int key, int modifiers) {
        if(!this.editable) return;

        if(Screen.isCopy(ch)) {
            String selection = getSelection();
            if(selection != null)
                MinecraftClient.getInstance().keyboard.setClipboard(selection);
            return;
        } else if(Screen.isPaste(ch)) {
            if(select != -1) {
                int a = select;
                int b = cursor;
                if(b < a) {
                    int tmp = b;
                    b = a;
                    a = tmp;
                }
                String before = this.text.substring(0, a);
                String after = this.text.substring(b);
                String clip = MinecraftClient.getInstance().keyboard.getClipboard();
                text = before + clip + after;
                select = -1;
                cursor = (before + clip).length();
            } else {
                String before = this.text.substring(0, cursor);
                String after = this.text.substring(cursor);
                String clip = MinecraftClient.getInstance().keyboard.getClipboard();
                text = before + clip + after;
                cursor += clip.length();
                if(text.length() > this.maxLength) {
                    text = text.substring(0, maxLength);
                    if(cursor > text.length())
                        cursor = text.length();
                }
            }
            if(onChanged != null)
                onChanged.accept(this.text);
            return;
        } else if(Screen.isSelectAll(ch)) {
            select = 0;
            cursor = text.length();
            return;
        }

        if(modifiers == 0) {
            if(ch == GLFW.GLFW_KEY_DELETE || ch == GLFW.GLFW_KEY_BACKSPACE) {
                if(text.length() > 0 && cursor > 0) {
                    if(select >= 0 && select != cursor) {
                        int a = select;
                        int b = cursor;
                        if(b < a) {
                            int tmp = b;
                            b = a;
                            a = tmp;
                        }
                        String before = this.text.substring(0, a);
                        String after = this.text.substring(b);
                        text = before + after;
                        if(cursor == b) cursor = a;
                        select = -1;
                    } else {
                        String before = this.text.substring(0, cursor);
                        String after = this.text.substring(cursor);
                        before = before.substring(0, before.length() - 1);
                        text = before + after;
                        cursor--;
                    }
                    if(onChanged != null)
                        onChanged.accept(this.text);
                }
            } else if(ch == GLFW.GLFW_KEY_LEFT) {
                if(select != -1) {
                    cursor = Math.min(cursor, select);
                    select = -1;
                } else {
                    if(cursor > 0)
                        cursor--;
                }
            } else if(ch == GLFW.GLFW_KEY_RIGHT) {
                if(select != -1) {
                    cursor = Math.max(cursor, select);
                } else {
                    if(cursor < text.length())
                        cursor++;
                 }
            }
        } else {
            if(modifiers == GLFW.GLFW_MOD_SHIFT) {
                if(ch == GLFW.GLFW_KEY_LEFT) {
                    if(select == -1) select = cursor;
                    if(cursor > 0) cursor--;
                    if(select == cursor) select = -1;
                } else if(ch == GLFW.GLFW_KEY_RIGHT) {
                    if(select == -1) select = cursor;
                    if(cursor < text.length()) cursor++;
                    if(select == cursor) select = -1;
                }
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static int getCaretPos(String s, int x) {
        if (x <= 0) return 0;

        TextRenderer font = MinecraftClient.getInstance().textRenderer;
        int lastAdvance = 0;
        for (int i = 0; i < s.length() - 1; i++) {
            int advance = font.getWidth(s.substring(0, i + 1));
            int charAdvance = advance - lastAdvance;
            if (x < advance + (charAdvance / 2))
                return i + 1;
            lastAdvance = advance;
        }
        return s.length();
    }

    @Environment(EnvType.CLIENT)
    public static int getCaretOffset(String s, int pos) {
        if (pos == 0) return 0;

        TextRenderer font = MinecraftClient.getInstance().textRenderer;
        int ofs = font.getWidth(s.substring(0, pos)) + 1;
        return ofs;
    }
}
