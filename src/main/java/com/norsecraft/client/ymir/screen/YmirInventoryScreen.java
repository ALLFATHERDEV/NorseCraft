package com.norsecraft.client.ymir.screen;

import com.norsecraft.client.ymir.YmirScreenDrawing;
import com.norsecraft.client.ymir.interpretation.GuiInterpretation;
import com.norsecraft.client.ymir.interpretation.SyncedGuiInterpretation;
import com.norsecraft.client.ymir.widget.YmirPanel;
import com.norsecraft.client.ymir.widget.YmirWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

/**
 * This class represents a synced gui screen
 * It will handle all the things from the GuiInterpretation
 *
 * @param <T> The synced interpretation
 */
public class YmirInventoryScreen<T extends SyncedGuiInterpretation> extends HandledScreen<T> implements YmirScreenImpl {

    /**
     * The synced gui interpretation
     */
    protected SyncedGuiInterpretation interpretation;

    /**
     * The last responder widget. If you clicked on a widget this widget is set as the last responder
     */
    @Nullable
    protected YmirWidget lastResponder = null;

    /**
     * The mouse input handler, that handles all the mouse inputs
     */
    private final MouseInputHandler<YmirInventoryScreen<T>> mouseInputHandler = new MouseInputHandler<>(this);

    /**
     * Default constructor without a title
     *
     * @param interpretation the gui interpretation
     * @param player         the player instance
     */
    public YmirInventoryScreen(T interpretation, PlayerEntity player) {
        this(interpretation, player, new LiteralText(""));
    }

    /**
     * Default constructor with a title
     *
     * @param interpretation the gui interpretation
     * @param player         the player instance
     * @param title          a title
     */
    public YmirInventoryScreen(T interpretation, PlayerEntity player, Text title) {
        super(interpretation, player.getInventory(), title);
        this.interpretation = interpretation;
        width = 18 * 9;
        height = 18 * 9;
        this.backgroundWidth = 18 * 9;
        this.backgroundHeight = 18 * 9;
        interpretation.getRootPanel().validate(interpretation);
    }

    /**
     * Default init method form {@link Screen}
     * This method initialize the root panel and the painters
     */
    @Override
    protected void init() {
        super.init();
        client.keyboard.setRepeatEvents(true);

        YmirPanel root = interpretation.getRootPanel();
        if (root != null)
            root.addPainters();
        interpretation.addPainters();

        reposition(width, height);
    }

    /**
     * If the screen gets closed this method will be called
     */
    @Override
    public void removed() {
        super.removed();
        this.client.keyboard.setRepeatEvents(false);
    }

    /**
     * @return the gui interpretation
     */
    @Override
    public GuiInterpretation getInterpretation() {
        return this.interpretation;
    }

    /**
     * @return the last responder. Can be null
     */
    @Override
    public @Nullable YmirWidget getLastResponder() {
        return this.lastResponder;
    }

    /**
     * Set the last responder
     *
     * @param lastResponder the last responder. Can be nul
     */
    @Override
    public void setLastResponder(@Nullable YmirWidget lastResponder) {
        this.lastResponder = lastResponder;
    }

    /**
     * Clears all the peers
     */
    private void clearPeers() {
        this.interpretation.slots.clear();
    }

    /**
     * If the minecraft screen gets resized this method will calculate the left and top position new
     *
     * @param screenWidth  the new screen width
     * @param screenHeight the new screen height
     */
    protected void reposition(int screenWidth, int screenHeight) {
        YmirPanel basePanel = interpretation.getRootPanel();
        if (basePanel != null) {
            clearPeers();
            basePanel.validate(interpretation);

            backgroundWidth = basePanel.getWidth();
            backgroundHeight = basePanel.getHeight();

            //DEBUG
            if (backgroundWidth < 16)
                backgroundWidth = 300;
            if (backgroundHeight < 16)
                backgroundHeight = 300;
        }

        titleX = interpretation.getTitlePos().x();
        titleY = interpretation.getTitlePos().y();

        if (!interpretation.isFullscreen()) {
            x = (screenWidth / 2) - (backgroundWidth / 2);
            y = (screenHeight / 2) - (backgroundHeight / 2);
        } else {
            x = 0;
            y = 0;

            if (basePanel != null)
                basePanel.setSize(screenWidth, screenHeight);
        }
    }

    /**
     * We don't want a pause screen, but if you want just overwrite it and return true
     * A pause screen pauses the game. i.e: You press ESCAPE
     *
     * @return true if it should be a pause screen or false if not
     */
    @Override
    public boolean isPauseScreen() {
        return false;
    }

    //==========================================================================
    //=                          INPUT HANDLING                                =
    //==========================================================================

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (interpretation.getFocus() == null) return false;
        interpretation.getFocus().onCharTyped(chr);
        return true;
    }

    @Override
    public boolean keyPressed(int ch, int scanCode, int modifiers) {
        if (ch == GLFW.GLFW_KEY_ESCAPE || ch == GLFW.GLFW_KEY_TAB)
            return super.keyPressed(ch, scanCode, modifiers);
        else {
            if (interpretation.getFocus() == null)
                return super.keyPressed(ch, scanCode, modifiers);
            else {
                interpretation.getFocus().onKeyPressed(ch, scanCode, modifiers);
                return true;
            }
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (interpretation.getFocus() == null)
            return false;
        interpretation.getFocus().onKeyReleased(keyCode, scanCode, modifiers);
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean result = super.mouseClicked(mouseX, mouseY, button);
        int containerX = (int) mouseX - x;
        int containerY = (int) mouseY - y;
        if (containerX < 0 || containerY < 0 || containerX >= width || containerY >= height) return result;
        mouseInputHandler.onMouseDown(containerX, containerY, button);

        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        int containerX = (int) mouseX - x;
        int containerY = (int) mouseY - y;
        mouseInputHandler.onMouseUp(containerX, containerY, mouseButton);

        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
        super.mouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);

        int containerX = (int) mouseX - x;
        int containerY = (int) mouseY - y;
        mouseInputHandler.onMouseDrag(containerX, containerY, mouseButton, deltaX, deltaY);

        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (interpretation.getRootPanel() == null) return super.mouseScrolled(mouseX, mouseY, amount);

        int containerX = (int) mouseX - x;
        int containerY = (int) mouseY - y;
        mouseInputHandler.onMouseScroll(containerX, containerY, amount);

        return true;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (interpretation.getRootPanel() == null) return;

        int containerX = (int) mouseX - x;
        int containerY = (int) mouseY - y;
        mouseInputHandler.onMouseMove(containerX, containerY);
    }

    @Override
    public void renderTextHover(MatrixStack matrices, @Nullable Style textStyle, int x, int y) {
        renderTextHoverEffect(matrices, textStyle, x, y);
    }


    /**
     * IN the default system you use this method to render all the background things like the background textures or something else.
     * But in the "Ymir" system we don't need that, because we use our own painting method
     *
     * @param matrices the render matrix from mc
     * @param delta    the delta time
     * @param mouseX   mouse X
     * @param mouseY   mouse Y
     */
    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {

    }

    /**
     * The actual render method for the root panel
     *
     * @param matrices the render matrix from mc
     * @param mouseX   the mouse x
     * @param mouseY   the mouse y
     */
    private void paint(MatrixStack matrices, int mouseX, int mouseY) {
        this.renderBackground(matrices);

        if (interpretation != null) {
            YmirPanel root = interpretation.getRootPanel();
            if (root != null) {
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                Scissors.refreshScissors();
                root.paint(matrices, x, y, mouseX - x, mouseY - y);
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                Scissors.checkStackIsEmpty();
            }
        }
    }

    /**
     * This is the render method from mc, where we call our paint method, and rendering the actual gui with all extras like the tool tips
     *
     * @param matrices the render matrix from mc
     * @param mouseX   mouse x
     * @param mouseY   mouse y
     * @param delta    the delta time
     */
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        paint(matrices, mouseX, mouseY);

        super.render(matrices, mouseX, mouseY, delta);
        DiffuseLighting.disableGuiDepthLighting();

        if (interpretation != null) {
            YmirPanel root = interpretation.getRootPanel();
            if (root != null) {
                YmirWidget hitChild = root.hit(mouseX - x, mouseY - y);
                if (hitChild != null)
                    hitChild.renderTooltip(matrices, x, y, mouseX - x, mouseY - y);
            }
        }

        drawMouseoverTooltip(matrices, mouseX, mouseY);

    }

    /**
     * This method is called after the "render" method and should be handle all text rendering
     *
     * @param matrices the render matrix from mc
     * @param mouseX   the mouse x
     * @param mouseY   the mouse y
     */
    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        if (interpretation != null && interpretation.isTitleVisible()) {
            int width = interpretation.getRootPanel().getWidth();
            YmirScreenDrawing.drawString(matrices, getTitle().asOrderedText(), interpretation.getTitleAlignment(), titleX, titleY, width, interpretation.getTitleColor());
        }
    }

    /**
     * Handles the screen ticking on the server side
     */
    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        if (interpretation != null) {
            YmirPanel root = interpretation.getRootPanel();
            if (root != null)
                root.tick();
        }
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        if (interpretation != null)
            interpretation.cycleFocus(lookForwards);
        return true;
    }

    @Override
    protected void addElementNarrations(NarrationMessageBuilder builder) {
        if (interpretation != null)
            NarrationHelper.addNarrations(interpretation.getRootPanel(), builder);
    }
}
