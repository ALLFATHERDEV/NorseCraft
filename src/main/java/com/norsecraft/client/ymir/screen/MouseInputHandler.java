package com.norsecraft.client.ymir.screen;

import com.norsecraft.client.ymir.widget.YmirWidget;
import com.norsecraft.client.ymir.widget.data.InputResult;
import com.norsecraft.client.ymir.widget.data.ObservableProperty;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * This class handles the mouse input screen for the screen S
 *
 * @param <S> the screen to handle the mouse input
 */
public final class MouseInputHandler<S extends Screen & YmirScreenImpl> {

    /**
     * The screen to handle the input
     */
    private final S screen;

    /**
     * The hovered property. For more information look  at the {@link ObservableProperty} class
     */
    private final ObservableProperty<@Nullable YmirWidget> hovered = ObservableProperty.<YmirWidget>of(null).build();

    /**
     * Default constructor that adds a listener to the hovered property
     *
     * @param screen the screen where it should listen to
     */
    public MouseInputHandler(S screen) {
        this.screen = screen;
        hovered.addListener((property, from, to) -> {
            if (from != null) from.setHovered(false);
            if (to != null) to.setHovered(true);
        });
    }

    /**
     * This method will be executed when the player clicked the mouse
     *
     * @param containerX  the x position of the gui container
     * @param containerY  the y position of the gui container
     * @param mouseButton the mouse button
     */
    public void onMouseDown(int containerX, int containerY, int mouseButton) {
        if (screen.getLastResponder() == null) {
            YmirWidget lastResponder = screen.getInterpretation().getRootPanel().hit(containerX, containerY);
            screen.setLastResponder(lastResponder);
            if (lastResponder != null)
                this.runTree(lastResponder,
                        widget -> widget.onMouseDown(containerX - widget.getAbsoluteX(), containerY - widget.getAbsoluteY(), mouseButton));
        }
    }

    /**
     * This method will be executed when the player released the finger from the mouse button
     *
     * @param containerX  the x position of the gui container
     * @param containerY  the y position of the gui container
     * @param mouseButton the mouse button
     */
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

    /**
     * If the player performs a drag action, this method will be executed
     *
     * @param containerX  the x position of the gui container
     * @param containerY  the y position of the gui container
     * @param mouseButton the mouse button
     * @param deltaX      the delta x from the start to the end
     * @param deltaY      the delta y from the start to the end
     */
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

    /**
     * If the player use the mouse wheel this method will be called
     *
     * @param containerX the x position of the gui container
     * @param containerY the y position of the gui container
     * @param amount this parameter has only two values:
     *               +1.0 for scrolling up
     *               -1.0 for scrolling down
     */
    public void onMouseScroll(int containerX, int containerY, double amount) {
        runTree(screen.getInterpretation().getRootPanel().hit(containerX, containerY),
                widget -> widget.onMouseScroll(containerX - widget.getAbsoluteX(), containerY - widget.getAbsoluteY(), amount));
    }

    /**
     * If the player moves the mouse this method will be executed
     *
     * @param containerX the x position of the gui container
     * @param containerY the y position of the gui container
     */
    public void onMouseMove(int containerX, int containerY) {
        YmirWidget w = screen.getInterpretation().getRootPanel().hit(containerX, containerY);
        runTree(w, widget -> widget.onMouseMove(containerX - widget.getAbsoluteX(), containerY - widget.getAbsoluteY()));

        @Nullable
        YmirWidget hoveredWidget = runTree(w,
                widget -> InputResult.of(widget.canHover() && widget.isWithinBounds(containerX - widget.getAbsoluteX(), containerY - widget.getAbsoluteY())));
        hovered.set(hoveredWidget);

    }

    /**
     * This method runs through the widget and all his parent widgets and returns the last found widget
     *
     * @param bottom the first widget
     * @param function the function for checking if the tree should continue
     * @return the last widget from all the parents (If it has more then one parent)
     */
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
