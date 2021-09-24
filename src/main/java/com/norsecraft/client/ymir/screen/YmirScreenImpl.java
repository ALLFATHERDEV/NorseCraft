package com.norsecraft.client.ymir.screen;

import com.norsecraft.client.ymir.interpretation.GuiInterpretation;
import com.norsecraft.client.ymir.widget.YmirWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import org.jetbrains.annotations.Nullable;

public interface YmirScreenImpl {

    GuiInterpretation getInterpretation();

    @Nullable YmirWidget getLastResponder();

    void setLastResponder(@Nullable YmirWidget lastResponder);

    void renderTextHover(MatrixStack matrices, @Nullable Style textStyle, int x, int y);

}
