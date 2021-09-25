package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.YmirScreenDrawing;
import com.norsecraft.client.ymir.widget.data.Axis;
import com.norsecraft.client.ymir.widget.data.InputResult;
import net.minecraft.client.util.math.MatrixStack;

public class YmirScrollBar extends YmirWidget {

    private static final int SCROLLING_SPEED = 4;

    protected Axis axis = Axis.HORIZONTAL;
    protected int value;
    protected int maxValue = 100;
    protected int window = 16;

    protected int anchor = -1;
    protected int anchorValue = -1;
    protected boolean sliding = false;

    public YmirScrollBar() {

    }

    public YmirScrollBar(Axis axis) {
        this.axis = axis;
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        YmirScreenDrawing.drawBeveledPanel(matrices, x, y, width, height, 0xFF_212121, 0xFF_2F2F2F, 0xFF_5D5D5D);
        if(maxValue < 0)
            return;

        int top, middle, bottom;
        if(sliding) {
            top = 0xFF_6C6C6C;
            middle = 0xFF_2F2F2F;
            bottom = 0xFF_212121;
        } else if(isWithinBounds(mouseX, mouseY)) {
            top = 0xFF_5F6A9D;
            middle = 0xFF_323F6E;
            bottom = 0xFF_0B204A;
        } else {
            top = 0xFF_6C6C6C;
            middle = 0xFF_414141;
            bottom = 0xFF_212121;
        }

        if(axis == Axis.HORIZONTAL) {
            YmirScreenDrawing.drawBeveledPanel(matrices, x + 1 + getHandlePosition(), y + 1, getHandleSize(), height - 2, top, middle, bottom);

            if(isFocused()) {
                drawBeveledOutline(matrices, x + 1 + getHandlePosition(), y + 1, getHandleSize(), height - 2, 0xFF_FFFFA7, 0xFF_8C8F39);
            }
        } else {
            YmirScreenDrawing.drawBeveledPanel(matrices, x + 1, y + 1 + getHandlePosition(), width - 2, getHandleSize(), top, middle, bottom);

            if(isFocused()) {
                drawBeveledOutline(matrices, x + 1, y + 1 + getHandlePosition(), width - 2, getHandleSize(), 0xFF_FFFFA7, 0xFF_8C8F39);
            }
        }

    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    private static void drawBeveledOutline(MatrixStack matrices, int x, int y, int width, int height, int topleft, int bottomright) {
        YmirScreenDrawing.coloredRect(matrices, x,             y,              width,     1,          topleft); //Top shadow
        YmirScreenDrawing.coloredRect(matrices, x,             y + 1,          1,         height - 1, topleft); //Left shadow
        YmirScreenDrawing.coloredRect(matrices, x + width - 1, y + 1,          1,         height - 1, bottomright); //Right hilight
        YmirScreenDrawing.coloredRect(matrices, x + 1,         y + height - 1, width - 1, 1,          bottomright); //Bottom hilight
    }

    public int getHandleSize() {
        float percentage = (window >= maxValue) ? 1f : window / (float) maxValue;
        int bar = (axis == Axis.HORIZONTAL) ? width - 2 : height - 2;
        int result = (int) (percentage * bar);
        if(result < 6)
            result = 6;
        return result;
    }

    public int getMovableDistance() {
        int bar = (axis == Axis.HORIZONTAL) ? width - 2 : height - 2;
        return bar - getHandleSize();
    }

    public int pixelsToValue(int pixels) {
        int bar = getMovableDistance();
        float percent = pixels / (float) bar;
        return (int)(percent * (maxValue - window));
    }

    public int getHandlePosition() {
        float percent = value / (float) Math.max(maxValue - window, 1);
        return (int) (percent * getMovableDistance());
    }

    public int getMaxScrollValue() {
        return maxValue - window;
    }

    protected void adjustSlider(int x, int y) {
        int delta = axis == Axis.HORIZONTAL ? x - anchor : y - anchor;

        int valueDelta = pixelsToValue(delta);
        int valueNew = anchorValue + valueDelta;

        if(valueNew > getMaxScrollValue())
            valueNew = getMaxScrollValue();
        if(valueNew < 0)
            valueNew = 0;
        this.value = valueNew;
    }

    @Override
    public InputResult onMouseDown(int x, int y, int button) {
        requestFocus();

        if(axis == Axis.HORIZONTAL) {
            anchor = x;
            anchorValue = value;
        } else {
            anchor = y;
            anchorValue = value;
        }
        sliding = true;
        return InputResult.PROCESSED;
    }

    @Override
    public InputResult onMouseDrag(int x, int y, int button, double deltaX, double deltaY) {
        adjustSlider(x, y);
        return InputResult.PROCESSED;
    }

    @Override
    public InputResult onMouseUp(int x, int y, int button) {
        anchor = -1;
        anchorValue = -1;
        sliding = false;
        return InputResult.PROCESSED;
    }

    @Override
    public void onKeyPressed(int ch, int key, int modifiers) {
        YmirAbstractSlider.Direction direction = axis == Axis.HORIZONTAL ? YmirAbstractSlider.Direction.RIGHT : YmirAbstractSlider.Direction.DOWN;

        if(YmirAbstractSlider.isIncreasingKey(ch, direction)) {
            if(value < getMaxScrollValue())
                value++;
        } else if(YmirAbstractSlider.isDecreasingKey(ch, direction)) {
            if(value > 0)
                value--;
        }
    }

    @Override
    public InputResult onMouseScroll(int x, int y, double amount) {
        setValue(getValue() + (int) -amount * SCROLLING_SPEED);
        return InputResult.PROCESSED;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        this.checkValue();
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        this.checkValue();
    }

    public void setWindow(int window) {
        this.window = window;
    }

    protected void checkValue() {
        if(this.value > maxValue - window) {
            this.value = maxValue - window;
        }
        if(this.value < 0)
            this.value = 0;
    }
}
