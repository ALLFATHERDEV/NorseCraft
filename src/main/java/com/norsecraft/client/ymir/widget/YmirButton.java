package com.norsecraft.client.ymir.widget;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.ymir.YmirScreenDrawing;
import com.norsecraft.client.ymir.widget.data.HorizontalAlignment;
import com.norsecraft.client.ymir.widget.data.InputResult;
import com.norsecraft.client.ymir.widget.data.NarrationMessages;
import com.norsecraft.client.ymir.widget.data.Texture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class YmirButton extends YmirWidget {

    private Text label;
    private boolean enabled = true;
    protected HorizontalAlignment alignment = HorizontalAlignment.CENTER;
    private final Texture texture;

    @Nullable
    private Runnable onClick;

    public YmirButton(Texture texture) {
        this.texture = texture;
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        YmirScreenDrawing.texturedGuiRect(matrices, x, y, width, height, texture);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public InputResult onClick(int x, int y, int button) {
        if (enabled && isWithinBounds(x, y)) {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

            if (onClick != null)
                onClick.run();
            return InputResult.PROCESSED;
        }
        return InputResult.IGNORED;
    }

    @Override
    public void onKeyPressed(int ch, int key, int modifiers) {
        if (isActivationKey(ch))
            onClick(0, 0, 0);
    }

    @Nullable
    public Runnable getOnClick() {
        return onClick;
    }

    public void setOnClick(@Nullable Runnable onClick) {
        this.onClick = onClick;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public YmirButton setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Text getLabel() {
        return label;
    }

    public YmirWidget setLabel(Text label) {
        this.label = label;
        return this;
    }

    public HorizontalAlignment getAlignment() {
        return alignment;
    }

    public YmirWidget setAlignment(HorizontalAlignment alignment) {
        this.alignment = alignment;
        return this;
    }

    @Override
    public void addNarrations(NarrationMessageBuilder builder) {
        builder.put(NarrationPart.TITLE, ClickableWidget.getNarrationMessage(getLabel()));
        if(isEnabled()) {
            if(isFocused()) {
                builder.put(NarrationPart.USAGE, NarrationMessages.BUTTON_USAGE_FOCUSED);
            } else if(isHovered())
                builder.put(NarrationPart.USAGE, NarrationMessages.BUTTON_USAGE_HOVERED);
        }
    }
}
