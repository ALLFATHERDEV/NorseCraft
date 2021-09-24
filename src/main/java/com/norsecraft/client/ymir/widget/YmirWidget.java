package com.norsecraft.client.ymir.widget;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.ymir.interpretation.GuiInterpretation;
import com.norsecraft.client.ymir.widget.data.InputResult;
import com.norsecraft.client.ymir.widget.data.ObservableProperty;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class YmirWidget {

    protected int x = 0;
    protected int y = 0;
    protected int width = 18;
    protected int height = 18;
    protected int additionalWidth = 0;
    protected int additionalHeight = 0;

    @Nullable
    protected YmirPanel parent;
    @Nullable
    protected GuiInterpretation host;

    protected final ObservableProperty<Boolean> hovered = ObservableProperty.<Boolean>of(false).nonnull().name("YmirWidget.hovered").build();

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int x, int y) {
        this.width = x;
        this.height = y;
    }

    public void setAdditionalSize(int x, int y) {
        this.additionalWidth = x;
        this.additionalHeight = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAdditionalHeight() {
        return additionalHeight;
    }

    public int getAdditionalWidth() {
        return additionalWidth;
    }

    public int getAbsoluteX() {
        if (parent == null)
            return getX();
        else
            return getX() + parent.getAbsoluteX();
    }

    public int getAbsoluteY() {
        if (parent == null)
            return getY();
        else
            return getY() + parent.getAbsoluteY();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean canResize() {
        return false;
    }

    @Nullable
    public YmirPanel getParent() {
        return parent;
    }

    @Environment(EnvType.CLIENT)
    public InputResult onMouseDown(int x, int y, int button) {
        return InputResult.IGNORED;
    }

    @Environment(EnvType.CLIENT)
    public InputResult onMouseDrag(int x, int y, int button, double deltaX, double deltaY) {
        return InputResult.IGNORED;
    }

    @Environment(EnvType.CLIENT)
    public InputResult onMouseUp(int x, int y, int button) {
        return InputResult.IGNORED;
    }

    @Environment(EnvType.CLIENT)
    public InputResult onClick(int x, int y, int button) {
        return InputResult.IGNORED;
    }

    @Environment(EnvType.CLIENT)
    public InputResult onMouseScroll(int x, int y, double amount) {
        return InputResult.IGNORED;
    }

    @Environment(EnvType.CLIENT)
    public InputResult onMouseMove(int x, int y) {
        return InputResult.IGNORED;
    }

    @Environment(EnvType.CLIENT)
    public void onCharTyped(char ch) {
    }

    @Environment(EnvType.CLIENT)
    public void onKeyPressed(int ch, int key, int modifiers) {
    }

    @Environment(EnvType.CLIENT)
    public void onKeyReleased(int ch, int key, int modifiers) {
    }

    public void onFocusGained() {
    }

    public void onFocusLost() {
    }

    public boolean isFocused() {
        if (host == null) return false;
        return host.isFocused(this);
    }

    public void requestFocus() {
        if (host != null) {
            host.requestFocus(this);
        } else {
            NorseCraftMod.LOGGER.warn("Requesting focus for {}, but the host is null", this);
        }
    }

    public void releaseFocus() {
        if (host != null)
            host.releaseFocus(this);
    }

    public boolean canFocus() {
        return false;
    }

    @Environment(EnvType.CLIENT)
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {

    }

    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < (this.width / 2) && y < (this.height / 2);
    }

    @Environment(EnvType.CLIENT)
    public void renderTooltip(MatrixStack matrices, int x, int y, int tX, int tY) {
        TooltipBuilder builder = new TooltipBuilder();
        addTooltip(builder);
        if (builder.size() == 0)
            return;

        Screen screen = MinecraftClient.getInstance().currentScreen;
        assert screen != null;
        screen.renderOrderedTooltip(matrices, builder.lines, tX + x, tY + y);
    }

    public void validate(GuiInterpretation host) {
        if (host != null)
            this.host = host;
        else
            NorseCraftMod.LOGGER.warn("Validating {} with a null host", this);
    }

    @Nullable
    public GuiInterpretation getHost() {
        return host;
    }

    public void setHost(@Nullable GuiInterpretation host) {
        this.host = host;
    }

    @Environment(EnvType.CLIENT)
    public void addTooltip(TooltipBuilder builder) {

    }

    public YmirWidget hit(int x, int y) {
        return this;
    }

    @Environment(EnvType.CLIENT)
    public void tick() {

    }

    @Nullable
    public YmirWidget cycleFocus(boolean lookForwards) {
        return canFocus() ? (isFocused() ? null : this) : null;
    }

    public void onShown() {

    }

    public void onHidden() {
        releaseFocus();
    }

    @Environment(EnvType.CLIENT)
    public void addPainters() {

    }

    public boolean canHover() {
        return true;
    }

    public ObservableProperty<Boolean> hooveredProperty() {
        return hovered;
    }

    public final boolean isHovered() {
        return hovered.get();
    }

    public final void setHovered(boolean hovered) {
        this.hovered.set(hovered);
    }

    public boolean isNarratable() {
        return true;
    }

    @Environment(EnvType.CLIENT)
    public void addNarrations(NarrationMessageBuilder builder) {

    }

    @Environment(EnvType.CLIENT)
    public static boolean isActivationKey(int ch) {
        return ch == GLFW.GLFW_KEY_ENTER || ch == GLFW.GLFW_KEY_KP_ENTER || ch == GLFW.GLFW_KEY_SPACE;
    }


}
