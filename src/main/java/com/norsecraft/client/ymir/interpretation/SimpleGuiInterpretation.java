package com.norsecraft.client.ymir.interpretation;

import com.norsecraft.client.ymir.widget.slot.TradeOutputSlot;
import com.norsecraft.client.ymir.widget.slot.ValidatedSlot;
import com.norsecraft.client.ymir.widget.YmirGridPanel;
import com.norsecraft.client.ymir.widget.YmirLabel;
import com.norsecraft.client.ymir.widget.YmirPanel;
import com.norsecraft.client.ymir.widget.YmirWidget;
import com.norsecraft.client.ymir.widget.data.HorizontalAlignment;
import com.norsecraft.client.ymir.widget.data.Insets;
import com.norsecraft.client.ymir.widget.data.Vec2i;
import net.minecraft.screen.PropertyDelegate;
import org.jetbrains.annotations.Nullable;

public class SimpleGuiInterpretation implements GuiInterpretation {

    protected YmirPanel rootPanel = new YmirGridPanel().setInsets(Insets.ROOT_PANEL);
    protected PropertyDelegate propertyDelegate;
    protected YmirWidget focus;
    protected int titleColor = YmirLabel.DEFAULT_TEXT_COLOR;
    protected boolean fullscreen = false;
    protected boolean titleVisible = true;
    protected HorizontalAlignment titleAlignment = HorizontalAlignment.LEFT;
    protected Vec2i titlePos = new Vec2i(8, 6);

    public SimpleGuiInterpretation() {
    }

    @Override
    public YmirPanel getRootPanel() {
        return this.rootPanel;
    }

    @Override
    public int getTitleColor() {
        return this.titleColor;
    }

    @Override
    public GuiInterpretation setRootPanel(YmirPanel panel) {
        this.rootPanel = panel;
        return this;
    }

    @Override
    public GuiInterpretation setTitleColor(int color) {
        this.titleColor = color;
        return this;
    }

    @Override
    public GuiInterpretation setPropertyDelegate(PropertyDelegate delegate) {
        this.propertyDelegate = delegate;
        return this;
    }

    @Override
    public void addSlotPeer(ValidatedSlot slot) {

    }

    @Override
    public void addOutputSlot(TradeOutputSlot slot) {

    }

    @Override
    public void addPainters() {

    }

    @Override
    public @Nullable PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    @Override
    public boolean isFocused(YmirWidget widget) {
        return widget == focus;
    }

    @Override
    public @Nullable YmirWidget getFocus() {
        return this.focus;
    }

    @Override
    public void requestFocus(YmirWidget widget) {
        if (focus == widget) return;
        if (!widget.canFocus()) return;
        if (focus != null) focus.onFocusLost();
        focus = widget;
        focus.onFocusGained();
    }

    @Override
    public void releaseFocus(YmirWidget widget) {
        if (focus == widget) {
            focus = null;
            widget.onFocusLost();
        }
    }

    @Override
    public boolean isFullscreen() {
        return this.fullscreen;
    }

    @Override
    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    @Override
    public boolean isTitleVisible() {
        return titleVisible;
    }

    @Override
    public void setTitleVisible(boolean titleVisible) {
        this.titleVisible = titleVisible;
    }

    @Override
    public HorizontalAlignment getTitleAlignment() {
        return this.titleAlignment;
    }

    @Override
    public void setTitleAlignment(HorizontalAlignment alignment) {
        this.titleAlignment = alignment;
    }

    @Override
    public Vec2i getTitlePos() {
        return this.titlePos;
    }

    @Override
    public void setTitlePos(Vec2i titlePos) {
        this.titlePos = titlePos;
    }
}
