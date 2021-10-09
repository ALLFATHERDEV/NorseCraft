package com.norsecraft.client.ymir.interpretation;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.ymir.widget.FocusHandler;
import com.norsecraft.client.ymir.widget.YmirPanel;
import com.norsecraft.client.ymir.widget.YmirWidget;
import com.norsecraft.client.ymir.widget.data.HorizontalAlignment;
import com.norsecraft.client.ymir.widget.data.Vec2i;
import com.norsecraft.client.ymir.widget.slot.TradeOutputSlot;
import com.norsecraft.client.ymir.widget.slot.ValidatedSlot;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents an interpretation of a gui.
 * If you want to create a gui DON'T use this class.
 * <p>
 * Use the {@link SimpleGuiInterpretation} for client sided guis or
 * {@link SyncedGuiInterpretation} for container guis (like chest containers or crafting inventories)
 */
public interface GuiInterpretation {

    /**
     * Texture location of the components
     */
    Identifier COMPONENTS = NorseCraftMod.ncTex("gui/gui_components.png");

    /**
     * @return the root panel
     */
    YmirPanel getRootPanel();

    /**
     * @return the title color
     */
    int getTitleColor();

    /**
     * Sets a new root panel.
     * This method must be called at the end of the gui creation. But only if you created a new panel, if you use the default one then you dont have to set it
     *
     * @param panel the new root panel
     * @return the interpretation object
     */
    GuiInterpretation setRootPanel(YmirPanel panel);

    /**
     * Set the title color
     *
     * @param color the color
     * @return the interpretation object
     */
    GuiInterpretation setTitleColor(int color);

    /**
     * Set the property delegate. this method will only be used on the {@link SyncedGuiInterpretation}.
     * Ignored on {@link SimpleGuiInterpretation}
     *
     * @param delegate the new property delegate
     * @return the interpretation object
     */
    GuiInterpretation setPropertyDelegate(PropertyDelegate delegate);

    /**
     * Add a "peer" slot.
     * A "Peer" slot based on the normal mc slot, but it has some extra functions.
     * Look at {@link ValidatedSlot} fore more information
     *
     * @param slot the slot object
     */
    void addSlotPeer(ValidatedSlot slot);

    /**
     * This method is only for adding trade output slots
     *
     * @param slot the trade output slot
     */
    void addOutputSlot(TradeOutputSlot slot);

    /**
     * If you have more than one background painter you can add it here
     */
    @Environment(EnvType.CLIENT)
    void addPainters();

    /**
     * @return the property delegate. Can be null
     */
    @Nullable
    PropertyDelegate getPropertyDelegate();

    /**
     * @param widget the widget that you want to check if it is focues
     * @return true if it is focues or false if not
     */
    boolean isFocused(YmirWidget widget);

    /**
     * @return the current focused widget. Or null if no widget is focused
     */
    @Nullable
    YmirWidget getFocus();

    /**
     * This method request the focus for the widget. If you call this method, that system will try to set the focus on the widget that you set as parameter
     *
     * @param widget the widget that you want to focus
     */
    void requestFocus(YmirWidget widget);

    /**
     * Removes the focues from the widget
     *
     * @param widget the widget object
     */
    void releaseFocus(YmirWidget widget);

    /**
     * This method cycles the focus of the widgets, when you press TAB
     *
     * @param lookForwards if true it will cycle forwards, if false then backwards
     */
    default void cycleFocus(boolean lookForwards) {
        FocusHandler.cycleFocus(this, lookForwards);
    }

    /**
     * @return true if the screen is a fullscreen or false if not
     */
    boolean isFullscreen();

    /**
     * Set the screen to a fullscreen
     *
     * @param fullscreen true for fullscreen false for not
     */
    void setFullscreen(boolean fullscreen);

    /**
     * @return true if the title is visible
     */
    boolean isTitleVisible();

    /**
     * Set the title visibility
     *
     * @param titleVisible true if the title should be visible false if not
     */
    void setTitleVisible(boolean titleVisible);

    /**
     * @return the title alignment
     */
    HorizontalAlignment getTitleAlignment();

    /**
     * Set the title alignment
     *
     * @param alignment the new alignment
     */
    void setTitleAlignment(HorizontalAlignment alignment);

    /**
     * @return the current tile position
     */
    Vec2i getTitlePos();

    /**
     * Set the title position
     *
     * @param titlePos the new title position
     */
    void setTitlePos(Vec2i titlePos);

}
