package com.norsecraft.client.ymir.widget;

import com.norsecraft.client.ymir.screen.Scissors;
import net.minecraft.client.util.math.MatrixStack;

/**
 * A panel that is clipped to only render widgets inside its bounds.
 */
public class YmirClippedPanel extends YmirPanel {

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        if(getBackgroundPainter() != null)
            getBackgroundPainter().paintBackground(matrices, x, y, this);

        Scissors.push(x, y, width, height);
        for(YmirWidget child : children)
            child.paint(matrices, x + child.getX(), y + child.getY(), mouseX - child.getX(), mouseY - child.getY());
        Scissors.pop();
    }
}
