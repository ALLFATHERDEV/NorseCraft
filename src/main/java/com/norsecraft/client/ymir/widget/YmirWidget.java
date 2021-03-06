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

/**
 * The base class for all widgets.
 */
public class YmirWidget {

    protected int x = 0;
    protected int y = 0;
    protected int width = 18;
    protected int height = 18;

    /**
     * The containing {@link GuiInterpretation} of this widget.
     */
    @Nullable
    protected YmirPanel parent;
    @Nullable
    protected GuiInterpretation host;

    protected final ObservableProperty<Boolean> hovered = ObservableProperty.<Boolean>of(false).nonnull().name("YmirWidget.hovered").build();

    /**
     * Sets the location of this widget relative to its parent.
     *
     * @param x the new X coordinate
     * @param y the new Y coordinate
     */
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the size of this widget.
     *
     * <p>Overriding methods may restrict one of the dimensions to be
     * a constant value, for example {@code super.setSize(x, 20)}.
     *
     * @param x the new width
     * @param y the new height
     */
    public void setSize(int x, int y) {
        this.width = x;
        this.height = y;
    }

    /**
     * Gets the X coordinate of this widget relative to its parent.
     *
     * @return the X coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the Y coordinate of this widget relative to its parent.
     *
     * @return the Y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the absolute X coordinate of this widget.
     *
     * @return the absolute X coordinate
     */
    public int getAbsoluteX() {
        if (parent == null) {
            return getX();
        } else {
            return getX() + parent.getAbsoluteX();
        }
    }

    /**
     * Gets the absolute Y coordinate of this widget.
     *
     * @return the absolute Y coordinate
     */
    public int getAbsoluteY() {
        if (parent == null) {
            return getY();
        } else {
            return getY() + parent.getAbsoluteY();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Checks whether this widget can be resized using {@link #setSize}.
     *
     * @return true if this widget can be resized, false otherwise
     */
    public boolean canResize() {
        return false;
    }

    /**
     * Gets the parent panel of this widget.
     *
     * @return the parent, or null if this widget has no parent
     */
    @Nullable
    public YmirPanel getParent() {
        return parent;
    }

    /**
     * Sets the parent panel of this widget.
     *
     * @param parent the new parent
     */
    public void setParent(YmirPanel parent) {
        this.parent = parent;
    }

    /**
     * Notifies this widget that the mouse has been pressed while inside its bounds
     *
     * @param x      The X coordinate of the event, in widget-space (0 is the left edge of this widget)
     * @param y      The Y coordinate of the event, in widget-space (0 is the top edge of this widget)
     * @param button The mouse button that was used. Button numbering is consistent with LWJGL Mouse (0=left, 1=right, 2=mousewheel click)
     * @return {@link InputResult#PROCESSED} if the event is handled, {@link InputResult#IGNORED} otherwise.
     */
    @Environment(EnvType.CLIENT)
    public InputResult onMouseDown(int x, int y, int button) {
        return InputResult.IGNORED;
    }

    /**
     * Notifies this widget that the mouse has been moved while pressed and inside its bounds.
     *
     * @param x      The X coordinate of the event, in widget-space (0 is the left edge of this widget)
     * @param y      The Y coordinate of the event, in widget-space (0 is the top edge of this widget)
     * @param button The mouse button that was used. Button numbering is consistent with LWJGL Mouse (0=left, 1=right, 2=mousewheel click)
     * @param deltaX The amount of dragging on the X axis
     * @param deltaY The amount of dragging on the Y axis
     * @return {@link InputResult#PROCESSED} if the event is handled, {@link InputResult#IGNORED} otherwise.
     */
    @Environment(EnvType.CLIENT)
    public InputResult onMouseDrag(int x, int y, int button, double deltaX, double deltaY) {
        return InputResult.IGNORED;
    }

    /**
     * Notifies this widget that the mouse has been released while inside its bounds
     *
     * @param x      The X coordinate of the event, in widget-space (0 is the left edge of this widget)
     * @param y      The Y coordinate of the event, in widget-space (0 is the top edge of this widget)
     * @param button The mouse button that was used. Button numbering is consistent with LWJGL Mouse (0=left, 1=right, 2=mousewheel click)
     * @return {@link InputResult#PROCESSED} if the event is handled, {@link InputResult#IGNORED} otherwise.
     */
    @Environment(EnvType.CLIENT)
    public InputResult onMouseUp(int x, int y, int button) {
        return InputResult.IGNORED;
    }

    /**
     * Notifies this widget that the mouse has been pressed and released, both while inside its bounds.
     *
     * @param x      The X coordinate of the event, in widget-space (0 is the left edge of this widget)
     * @param y      The Y coordinate of the event, in widget-space (0 is the top edge of this widget)
     * @param button The mouse button that was used. Button numbering is consistent with LWJGL Mouse (0=left, 1=right, 2=mousewheel click)
     * @return {@link InputResult#PROCESSED} if the event is handled, {@link InputResult#IGNORED} otherwise.
     */
    @Environment(EnvType.CLIENT)
    public InputResult onClick(int x, int y, int button) {
        return InputResult.IGNORED;
    }

    /**
     * Notifies this widget that the mouse has been scrolled inside its bounds.
     *
     * @param x      The X coordinate of the event, in widget-space (0 is the left edge of this widget)
     * @param y      The Y coordinate of the event, in widget-space (0 is the top edge of this widget)
     * @param amount The scrolled amount. Positive values are up and negative values are down.
     * @return {@link InputResult#PROCESSED} if the event is handled, {@link InputResult#IGNORED} otherwise.
     */
    @Environment(EnvType.CLIENT)
    public InputResult onMouseScroll(int x, int y, double amount) {
        return InputResult.IGNORED;
    }

    /**
     * Notifies this widget that the mouse has been moved while inside its bounds.
     *
     * @param x The X coordinate of the event, in widget-space (0 is the left edge of this widget)
     * @param y The Y coordinate of the event, in widget-space (0 is the top edge of this widget)
     * @return {@link InputResult#PROCESSED} if the event is handled, {@link InputResult#IGNORED} otherwise.
     */
    @Environment(EnvType.CLIENT)
    public InputResult onMouseMove(int x, int y) {
        return InputResult.IGNORED;
    }

    /**
     * Notifies this widget that a character has been typed. This method is subject to key repeat,
     * and may be called for characters that do not directly have a corresponding keyboard key.
     * @param ch the character typed
     */
    @Environment(EnvType.CLIENT)
    public void onCharTyped(char ch) {
    }

    /**
     * Notifies this widget that a key has been pressed.
     * @param key the GLFW scancode of the key
     */
    @Environment(EnvType.CLIENT)
    public void onKeyPressed(int ch, int key, int modifiers) {
    }

    /**
     * Notifies this widget that a key has been released
     * @param key the GLFW scancode of the key
     */
    @Environment(EnvType.CLIENT)
    public void onKeyReleased(int ch, int key, int modifiers) {
    }

    /** Notifies this widget that it has gained focus */
    public void onFocusGained() {
    }

    /** Notifies this widget that it has lost focus */
    public void onFocusLost() {
    }

    /**
     * Tests whether this widget has focus.
     *
     * @return true if this widget widget has focus, false otherwise
     */
    public boolean isFocused() {
        if (host == null) return false;
        return host.isFocused(this);
    }

    /**
     * If this widget has a host, requests the focus from the host.
     */
    public void requestFocus() {
        if (host != null) {
            host.requestFocus(this);
        } else {
            NorseCraftMod.LOGGER.warn("Requesting focus for {}, but the host is null", this);
        }
    }

    /**
     * If this widget has a host, releases this widget's focus.
     */
    public void releaseFocus() {
        if (host != null)
            host.releaseFocus(this);
    }
    /**
     * Tests whether this widget can have the focus in the GUI.
     *
     * @return true if this widget can be focused, false otherwise
     */
    public boolean canFocus() {
        return false;
    }

    /**
     * Paints this widget.
     *
     * @param matrices the rendering matrix stack
     * @param x        this widget's X coordinate on the screen
     * @param y        this widget's Y coordinate on the screen
     * @param mouseX   the X coordinate of the cursor
     * @param mouseY   the X coordinate of the cursor
     */
    @Environment(EnvType.CLIENT)
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {

    }

    /**
     * Checks whether a location is within this widget's bounds.
     *
     * The default implementation checks that X and Y are at least 0 and below the width and height of this widget.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @return true if the location is within this widget, false otherwise
     */
    public boolean isWithinBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < (this.width / 2) && y < (this.height / 2);
    }

    /**
     * Internal method to render tooltip data. This requires an overridden {@link #addTooltip(TooltipBuilder)
     * addTooltip} method to insert data into the tooltip - without this, the method returns early because of no work.
     *
     * @param x  the X coordinate of this widget on screen
     * @param y  the Y coordinate of this widget on screen
     * @param tX the X coordinate of the tooltip
     * @param tY the Y coordinate of the tooltip
     */
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

    /**
     * Creates component peers, lays out children, and initializes animation data for this Widget and all its children.
     * The host container must clear any heavyweight peers from its records before this method is called.
     *
     * @param host the host GUI description
     */
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

    /**
     * Returns whether the user is hovering over this widget.
     * The result is an <i>observable property</i> that can be modified and listened to.
     *
     * This property takes into account {@link #isWithinBounds(int, int)} to check
     * if the cursor is within the bounds, as well as {@link #canHover()} to enable hovering at all.
     *
     * For rendering, it might be preferable that you check the mouse coordinates in
     * {@link #paint(MatrixStack, int, int, int, int) paint()} directly.
     * That lets you react to different parts of the widget being hovered over.
     *
     * @return the {@code hovered} property
     * @see #canHover()
     * @see #isHovered()
     * @see #setHovered(boolean)
     */
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
