package com.norsecraft.client.ymir.screen;

import com.norsecraft.client.ymir.interpretation.GuiInterpretation;
import com.norsecraft.client.ymir.widget.YmirWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import org.jetbrains.annotations.Nullable;

/**
 * An own implementation for our screens
 */
public interface YmirScreenImpl {

    /**
     * @return the current interpretation
     */
    GuiInterpretation getInterpretation();

    /**
     * @return the last responder
     */
    @Nullable YmirWidget getLastResponder();

    /**
     * Set the last responder
     *
     * @param lastResponder the new last responder
     */
    void setLastResponder(@Nullable YmirWidget lastResponder);

    /**
     * Render the text hover effects
     *
     * @param matrices the render matrix from mc
     * @param textStyle the text style, can be null
     * @param x the x position
     * @param y the y position
     */
    void renderTextHover(MatrixStack matrices, @Nullable Style textStyle, int x, int y);

}
