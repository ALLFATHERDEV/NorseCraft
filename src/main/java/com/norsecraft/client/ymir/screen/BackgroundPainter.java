package com.norsecraft.client.ymir.screen;

import com.norsecraft.client.ymir.YmirScreenDrawing;
import com.norsecraft.client.ymir.widget.YmirItemSlot;
import com.norsecraft.client.ymir.widget.YmirWidget;
import com.norsecraft.client.ymir.widget.data.Texture;
import net.minecraft.client.util.math.MatrixStack;

/**
 * This class represent a background painter.
 * All the different gui widgets (listed in the widget packet) can have an own background.
 * If you have a textured background just use the {@link TexturedBackgroundPainter}
 * If you want a extra special own background for something, you have to write a own background painter
 *
 */
public interface BackgroundPainter {

    /**
     * This method will be paint the background
     * @param matrices the render matrix from mc
     * @param left the x position
     * @param top the y positin
     * @param panel the root panel
     */
    void paintBackground(MatrixStack matrices, int left, int top, YmirWidget panel);

    /**
     * If you use a texture you have to return the texture here
     *
     * @return the used texture
     */
    default Texture getTexture() {
        return null;
    }

    /**
     * This is a background painter for default slot
     */
    BackgroundPainter SLOT = ((matrices, left, top, panel) -> {
        if(!(panel instanceof YmirItemSlot slot)) {
            YmirScreenDrawing.drawBeveledPanel(matrices, left - 1, top - 1, panel.getWidth() + 2, panel.getHeight() + 2, 0xB8000000, 0x4C000000, 0xB8FFFFFF);
        } else {
            for(int x = 0; x < slot.getWidth() / 18; ++x) {
                for(int y = 0; y < slot.getHeight() / 18; ++y) {
                    int index = x + y * (slot.getWidth() / 18);
                    int lo = 0xB8000000;
                    int bg = 0x4C000000;
                    int hi = 0xB8FFFFFF;
                    if(slot.isBigSlot()) {
                        YmirScreenDrawing.drawBeveledPanel(matrices, (x * 18) + left - 4, (y * 18) + top - 4, 26, 26,
                                lo, bg, hi);
                        if(slot.getFocusedSlot() == index) {
                            int sx = (x * 18) + left - 4;
                            int sy = (y * 18) + top - 4;
                            YmirScreenDrawing.coloredRect(matrices, sx,          sy,          26,     1,      0xFF_FFFFA0);
                            YmirScreenDrawing.coloredRect(matrices, sx,          sy + 1,      1,      26 - 1, 0xFF_FFFFA0);
                            YmirScreenDrawing.coloredRect(matrices, sx + 26 - 1, sy + 1,      1,      26 - 1, 0xFF_FFFFA0);
                            YmirScreenDrawing.coloredRect(matrices, sx + 1,      sy + 26 - 1, 26 - 1, 1,      0xFF_FFFFA0);
                        }
                    }else {
                        YmirScreenDrawing.drawBeveledPanel(matrices, (x * 18) + left, (y * 18) + top, 16+2, 16+2,
                                lo, bg, hi);
                        if (slot.getFocusedSlot() == index) {
                            int sx = (x * 18) + left;
                            int sy = (y * 18) + top;
                            YmirScreenDrawing.coloredRect(matrices, sx,          sy,          18,     1,      0xFF_FFFFA0);
                            YmirScreenDrawing.coloredRect(matrices, sx,          sy + 1,      1,      18 - 1, 0xFF_FFFFA0);
                            YmirScreenDrawing.coloredRect(matrices, sx + 18 - 1, sy + 1,      1,      18 - 1, 0xFF_FFFFA0);
                            YmirScreenDrawing.coloredRect(matrices, sx + 1,      sy + 18 - 1, 18 - 1, 1,      0xFF_FFFFA0);
                        }
                    }
                }
            }
        }
    });

}
