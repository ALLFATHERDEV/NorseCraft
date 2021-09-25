package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.widget.data.Axis;
import com.norsecraft.client.ymir.widget.data.InputResult;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.function.IntConsumer;

public abstract class YmirAbstractSlider extends YmirWidget {

    private static final int DRAGGING_FINISHED_RATE_LIMIT_FOR_SCROLLING = 10;

    protected int min;
    protected int max;
    protected final Axis axis;
    protected Direction direction;
    protected int value;
    protected boolean dragging = false;
    protected float valueToCoordRatio;
    protected float coordToValueRation;
    private boolean pendingDraggingFinishedFromKeyboard = false;
    private int draggingFinishedFromScrollingTimer = 0;
    private boolean pendingDraggingFinishedFromScrolling = false;

    @Nullable
    private IntConsumer valueChangeListener = null;
    @Nullable
    private IntConsumer draggingFinishedListener = null;

    protected YmirAbstractSlider(int min, int max, Axis axis) {
        if (max <= min) throw new IllegalArgumentException("Minimum value must be smaller than the maximum!");

        this.min = min;
        this.max = max;
        this.axis = axis;
        this.value = min;
        this.direction = (axis == Axis.HORIZONTAL) ? Direction.RIGHT : Direction.UP;
    }

    protected abstract int getThumbWidth();

   protected abstract boolean isMouseInsideBounds(int x, int y);

    @Override
    public void setSize(int x, int y) {
        super.setSize(x, y);
        int trackHeight = (axis == Axis.HORIZONTAL ? x : y) - getThumbWidth();
        valueToCoordRatio = (float) (max - min) / trackHeight;
        coordToValueRation = 1 / valueToCoordRatio;
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
    public InputResult onMouseDown(int x, int y, int button) {
        if(isMouseInsideBounds(x, y)) {
            requestFocus();
            return InputResult.PROCESSED;
        }
        return InputResult.IGNORED;
    }

    @Override
    public InputResult onMouseDrag(int x, int y, int button, double deltaX, double deltaY) {
        if(isFocused()) {
            dragging = true;
            moveSlider(x, y);
            return InputResult.PROCESSED;
        }
        return InputResult.IGNORED;
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        moveSlider(x, y);
        if(draggingFinishedListener != null)
            draggingFinishedListener.accept(value);
        return InputResult.PROCESSED;
    }

    private void moveSlider(int x, int y) {
        int axisPos = switch (direction) {
            case UP -> height - y;
            case DOWN -> y;
            case LEFT -> width - x;
            case RIGHT -> x;
        };

        int pos = axisPos - getThumbWidth() / 2;
        int rawValue = min + Math.round(valueToCoordRatio * pos);
        int previousValue = value;
        value = MathHelper.clamp(rawValue, min, max);
        if(value != previousValue)
            onValueChanged(value);
    }

    @Override
    public InputResult onMouseUp(int x, int y, int button) {
        dragging = false;
        if(draggingFinishedListener != null)
            draggingFinishedListener.accept(value);
        return InputResult.PROCESSED;
    }

    @Override
    public InputResult onMouseScroll(int x, int y, double amount) {
        if(direction == Direction.LEFT || direction == Direction.DOWN)
            amount = -amount;

        int previous = value;
        value = MathHelper.clamp(value + (int) Math.signum(amount) * MathHelper.ceil(valueToCoordRatio * Math.abs(amount) * 2), min, max);
        if(previous != value) {
            onValueChanged(value);
            pendingDraggingFinishedFromScrolling = true;
        }
        return InputResult.PROCESSED;
    }

    @Override
    public void tick() {
        if(draggingFinishedFromScrollingTimer > 0)
            draggingFinishedFromScrollingTimer--;

        if(pendingDraggingFinishedFromScrolling && draggingFinishedFromScrollingTimer <= 0) {
            if(draggingFinishedListener != null)
                draggingFinishedListener.accept(value);
            pendingDraggingFinishedFromScrolling = false;
            draggingFinishedFromScrollingTimer = DRAGGING_FINISHED_RATE_LIMIT_FOR_SCROLLING;
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.setValue(value, false);
    }

    public void setValue(int value, boolean callListeners) {
        int previous = this.value;
        this.value = MathHelper.clamp(value, min, max);
        if(callListeners && previous != this.value) {
            onValueChanged(this.value);
            if(draggingFinishedListener != null)
                draggingFinishedListener.accept(value);
        }
    }

    @Nullable
    public IntConsumer getValueChangeListener() {
        return valueChangeListener;
    }

    public void setValueChangeListener(@Nullable IntConsumer valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }

    @Nullable
    public IntConsumer getDraggingFinishedListener() {
        return draggingFinishedListener;
    }

    public void setDraggingFinishedListener(@Nullable IntConsumer draggingFinishedListener) {
        this.draggingFinishedListener = draggingFinishedListener;
    }

    public int getMinValue() {
        return min;
    }

    public int getMaxValue() {
        return max;
    }

    public void setMinValue(int min) {
        this.min = min;
        if(this.value < min) {
            this.value = min;
            onValueChanged(this.value);
        }
    }

    public void setMaxValue(int max) {
        this.max = max;
        if(this.value > max) {
            this.value = max;
            onValueChanged(this.value);
        }
    }

    public Axis getAxis() {
        return axis;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        if(direction.getAxis() != axis) {
            throw new IllegalArgumentException("Incorrect axis: " + axis);
        }

        this.direction = direction;
    }

    protected void onValueChanged(int value) {
        if(valueChangeListener != null)
            valueChangeListener.accept(value);
    }

    @Override
    public void onKeyPressed(int ch, int key, int modifiers) {
        boolean valueChanged = false;
        if(modifiers == 0) {
            if(isDecreasingKey(ch, direction) && value > min) {
                value--;
                valueChanged = true;
            } else if(isIncreasingKey(ch, direction) && value < min) {
                value++;
                valueChanged = true;
            }
        } else if(modifiers == GLFW.GLFW_MOD_CONTROL) {
            if(isDecreasingKey(ch, direction) && value != min) {
                value = min;
                valueChanged = true;
            } else if(isIncreasingKey(ch, direction) && value != min) {
                value = max;
                valueChanged = true;
            }
        }

        if(valueChanged) {
            onValueChanged(value);
            pendingDraggingFinishedFromKeyboard = true;
        }
    }

    @Override
    public void onKeyReleased(int ch, int key, int modifiers) {
        if(pendingDraggingFinishedFromKeyboard && (isDecreasingKey(ch, direction) || isIncreasingKey(ch, direction))) {
            if(draggingFinishedListener != null)
                draggingFinishedListener.accept(value);
            pendingDraggingFinishedFromKeyboard = false;
        }
    }

    public boolean isDragging() {
        return dragging;
    }

    public static boolean isDecreasingKey(int ch, Direction direction) {
        return direction.isInverted()
                ? (ch == GLFW.GLFW_KEY_RIGHT || ch == GLFW.GLFW_KEY_UP)
                : (ch == GLFW.GLFW_KEY_LEFT || ch == GLFW.GLFW_KEY_DOWN);
    }

    public static boolean isIncreasingKey(int ch, Direction direction) {
        return direction.isInverted()
                ? (ch == GLFW.GLFW_KEY_LEFT || ch == GLFW.GLFW_KEY_DOWN)
                : (ch == GLFW.GLFW_KEY_RIGHT || ch == GLFW.GLFW_KEY_UP);
    }

    public enum Direction {
        UP(Axis.VERTICAL, false),
        DOWN(Axis.VERTICAL, true),
        LEFT(Axis.HORIZONTAL, true),
        RIGHT(Axis.HORIZONTAL, false);

        private final Axis axis;
        private final boolean inverted;

        Direction(Axis axis, boolean inverted) {
            this.axis = axis;
            this.inverted = inverted;
        }


        public Axis getAxis() {
            return axis;
        }

        public boolean isInverted() {
            return inverted;
        }
    }

}
