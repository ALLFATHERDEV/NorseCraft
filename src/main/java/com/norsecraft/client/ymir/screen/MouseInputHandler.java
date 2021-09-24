package com.norsecraft.client.ymir.screen;

import com.norsecraft.client.ymir.widget.YmirWidget;
import com.norsecraft.client.ymir.widget.data.InputResult;
import com.norsecraft.client.ymir.widget.data.ObservableProperty;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public final class MouseInputHandler<S extends Screen & YmirScreenImpl> {

    private final S screen;
    private final ObservableProperty<@Nullable YmirWidget> hovered = ObservableProperty.<YmirWidget>of(null).build();

    public MouseInputHandler(S screen) {
        this.screen = screen;
        hovered.addListener((property, from, to) -> {
            if (from != null) from.setHovered(false);
            if (to != null) to.setHovered(true);
        });
    }

    public void onMouseDown(int containerX, int containerY, int mouseButton) {
        if (screen.getLastResponder() == null) {
            YmirWidget lastResponder = screen.getInterpretation().getRootPanel().hit(containerX, containerY);
            screen.setLastResponder(lastResponder);
            if (lastResponder != null)
                this.runTree(lastResponder,
                        widget -> widget.onMouseDown(containerX - widget.getAbsoluteX(), containerY - widget.getAbsoluteY(), mouseButton));
        }
    }

    public void onMouseUp(int containerX, int containerY, int mouseButton) {
        YmirWidget lastResponder = screen.getLastResponder();
        if (lastResponder != null) {
            int width = screen.width;
            int height = screen.height;

            runTree(lastResponder,
                    widget -> widget.onMouseUp(containerX - widget.getAbsoluteX(), containerY - widget.getAbsoluteY(), mouseButton));

            if (containerX >= 0 && containerY >= 0 && containerX < width && containerY < height) {
                runTree(lastResponder,
                        widget -> widget.onClick(containerX - widget.getAbsoluteX(), containerY - widget.getAbsoluteY(), mouseButton));
            }
        } else {
            runTree(screen.getInterpretation().getRootPanel().hit(containerX, containerY),
                    widget -> widget.onMouseUp(containerX - widget.getAbsoluteX(), containerY - widget.getAbsoluteY(), mouseButton));
        }
        screen.setLastResponder(null);
    }

    public void onMouseDrag(int containerX, int containerY, int mouseButton, double deltaX, double deltaY) {
        YmirWidget lastResponder = screen.getLastResponder();
        if (lastResponder != null) {
            lastResponder.onMouseDrag(containerX - lastResponder.getAbsoluteX(), containerY - lastResponder.getAbsoluteY(), mouseButton, deltaX, deltaY);
        } else {
            int width = screen.width;
            int height = screen.height;

            if (containerX < 0 || containerY < 0 || containerX >= width || containerY >= height) return;

            runTree(screen.getInterpretation().getRootPanel().hit(containerX, containerY),
                    widget -> widget.onMouseDrag(containerX - widget.getAbsoluteX(), containerY - widget.getAbsoluteY(), mouseButton, deltaX, deltaY));
        }
    }

    public void onMouseScroll(int containerX, int containerY, double amount) {
        runTree(screen.getInterpretation().getRootPanel().hit(containerX, containerY),
                widget -> widget.onMouseScroll(containerX - widget.getAbsoluteX(), containerY - widget.getAbsoluteY(), amount));
    }

    public void onMouseMove(int containerX, int containerY) {
        YmirWidget w = screen.getInterpretation().getRootPanel().hit(containerX, containerY);
        runTree(w, widget -> widget.onMouseMove(containerX - widget.getAbsoluteX(), containerY - widget.getAbsoluteY()));

        @Nullable
        YmirWidget hoveredWidget = runTree(w,
                widget -> InputResult.of(widget.canHover() && widget.isWithinBounds(containerX - widget.getAbsoluteX(), containerY - widget.getAbsoluteY())));
        hovered.set(hoveredWidget);

    }

    @Nullable
    private YmirWidget runTree(YmirWidget bottom, Function<YmirWidget, InputResult> function) {
        YmirWidget current = bottom;

        while (current != null) {
            InputResult result = function.apply(current);
            if (result == InputResult.PROCESSED)
                break;
            else
                current = current.getParent();
        }
        return current;

    }

}
