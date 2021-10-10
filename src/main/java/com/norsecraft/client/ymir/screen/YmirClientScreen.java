package com.norsecraft.client.ymir.screen;

import com.norsecraft.client.ymir.YmirScreenDrawing;
import com.norsecraft.client.ymir.interpretation.GuiInterpretation;
import com.norsecraft.client.ymir.widget.YmirPanel;
import com.norsecraft.client.ymir.widget.YmirWidget;
import com.norsecraft.common.util.VisualLogger;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

/**
 * This class represent a client sided screen.
 * It will handle all the things from the GuiInterpretation
 */
public class YmirClientScreen extends Screen implements YmirScreenImpl {

    /**
     * The gui interpretation
     */
    protected GuiInterpretation interpretation;

    /**
     * The left position of the screen
     */
    protected int left = 0;

    /**
     * The right position of the screen
     */
    protected int top = 0;

    /**
     * Title x position
     */
    protected int titleX;

    /**
     * Title y position
     */
    protected int titleY;

    /**
     * The last responder widget. If you clicked on a widget this widget is set as the last responder
     */
    @Nullable
    protected YmirWidget lastResponder = null;

    /**
     * The mouse input handler, that handles all the mouse inputs
     */
    private final MouseInputHandler<YmirClientScreen> mouseInputHandler = new MouseInputHandler<>(this);

    /**
     * Constructor without a title
     *
     * @param interpretation the interpretation for the screen
     */
    public YmirClientScreen(GuiInterpretation interpretation) {
        this(new LiteralText(""), interpretation);
    }

    /**
     * Constructor with title
     *
     * @param title          the gui title
     * @param interpretation the interpretation for the screen
     */
    public YmirClientScreen(Text title, GuiInterpretation interpretation) {
        super(title);
        this.interpretation = interpretation;
        interpretation.getRootPanel().validate(interpretation);
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

    /**
     * @return the gui interpretation
     */
    @Override
    public GuiInterpretation getInterpretation() {
        return this.interpretation;
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
        VisualLogger.reset();
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
     * If the minecraft screen gets resized this method will calculate the left and top position new
     *
     * @param screenWidth  the new screen width
     * @param screenHeight the new screen height
     */
    protected void reposition(int screenWidth, int screenHeight) {
        if (interpretation != null) {
            YmirPanel root = interpretation.getRootPanel();
            if (root != null) {
                titleX = interpretation.getTitlePos().x();
                titleY = interpretation.getTitlePos().y();

                if (!interpretation.isFullscreen()) {
                    this.left = ((screenWidth - root.getWidth()) / 2);
                    this.top = ((screenHeight - root.getHeight()) / 2);
                } else {
                    this.left = 0;
                    this.top = 0;

                    root.setSize(screenWidth, screenHeight);
                }
            }
        }
    }

    /**
     * This method paints the root panel and the title
     *
     * @param matrices the render matrix from mc
     * @param mouseX   the mouse x
     * @param mouseY   the mouse y
     */
    private void paint(MatrixStack matrices, int mouseX, int mouseY) {
        renderBackground(matrices);
        if (interpretation != null) {
            YmirPanel root = interpretation.getRootPanel();
            if (root != null) {
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                Scissors.refreshScissors();
                root.paint(matrices, left, top, mouseX - left, mouseY - top);
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                Scissors.checkStackIsEmpty();
            }

            if (getTitle() != null && interpretation.isTitleVisible()) {
                int width = interpretation.getRootPanel().getWidth();
                YmirScreenDrawing.drawString(matrices, getTitle().asOrderedText(), interpretation.getTitleAlignment(), left + titleX, top + titleY, width, interpretation.getTitleColor());
            }
        }
    }

    /**
     * Render method from {@link Screen}
     * Here we call our paint method from above, and render the current tooltip from a widget that is hovered. (If the widget has a tooltip)
     *
     * @param matrices the render matrix from mc
     * @param mouseX the mouse x
     * @param mouseY the mouse y
     * @param delta the delta time
     */
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        paint(matrices, mouseX, mouseY);

        super.render(matrices, mouseX, mouseY, delta);

        if (interpretation != null) {
            YmirPanel root = interpretation.getRootPanel();
            if (root != null) {
                YmirWidget hitChild = root.hit(mouseX - left, mouseY - top);
                if (hitChild != null)
                    hitChild.renderTooltip(matrices, left, top, mouseX - left, mouseY - top);
            }
        }

        VisualLogger.render(matrices);
    }

    /**
     * This method will be executed in the tick method from the client
     */
    @Override
    public void tick() {
        super.tick();
        if (interpretation != null) {
            YmirPanel root = interpretation.getRootPanel();
            if (root != null) {
                root.tick();
            }
        }
    }

    //==========================================================================
    //=                          INPUT HANDLING                                =
    //==========================================================================

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (interpretation.getRootPanel() == null) return super.mouseClicked(mouseX, mouseY, button);
        YmirWidget focus = interpretation.getFocus();
        if (focus != null) {
            int wx = focus.getAbsoluteX();
            int wy = focus.getAbsoluteY();

            if (mouseX >= wx && mouseX < wx + focus.getWidth() && mouseY >= wy && mouseY < wy + focus.getHeight()) {
                //Do nothing, focus will get the click soon
            } else {
                //Invalidate the component first
                interpretation.releaseFocus(focus);
            }
        }

        super.mouseClicked(mouseX, mouseY, button);
        int containerX = (int) mouseX - left;
        int containerY = (int) mouseY - top;
        if (containerX < 0 || containerY < 0 || containerX >= width || containerY >= height) return true;
        mouseInputHandler.onMouseDown(containerX, containerY, button);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (interpretation.getRootPanel() == null) return super.mouseReleased(mouseX, mouseY, button);
        super.mouseReleased(mouseX, mouseY, button);
        int containerX = (int) mouseX - left;
        int containerY = (int) mouseY - top;
        mouseInputHandler.onMouseUp(containerX, containerY, button);
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
        if (interpretation.getRootPanel() == null)
            return super.mouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
        super.mouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);

        int containerX = (int) mouseX - left;
        int containerY = (int) mouseY - top;
        mouseInputHandler.onMouseDrag(containerX, containerY, mouseButton, deltaX, deltaY);

        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (interpretation.getRootPanel() == null) return super.mouseScrolled(mouseX, mouseY, amount);

        int containerX = (int) mouseX - left;
        int containerY = (int) mouseY - top;
        mouseInputHandler.onMouseScroll(containerX, containerY, amount);

        return true;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (interpretation.getRootPanel() == null) return;

        int containerX = (int) mouseX - left;
        int containerY = (int) mouseY - top;
        mouseInputHandler.onMouseMove(containerX, containerY);
    }

    @Override
    public boolean charTyped(char ch, int keyCode) {
        if (interpretation.getFocus() == null) return false;
        interpretation.getFocus().onCharTyped(ch);
        return true;
    }

    @Override
    public boolean keyPressed(int ch, int keyCode, int modifiers) {
        if (super.keyPressed(ch, keyCode, modifiers)) return true;
        if (interpretation.getFocus() == null) return false;
        interpretation.getFocus().onKeyPressed(ch, keyCode, modifiers);
        return true;
    }

    @Override
    public boolean keyReleased(int ch, int keyCode, int modifiers) {
        if (interpretation.getFocus() == null) return false;
        interpretation.getFocus().onKeyReleased(ch, keyCode, modifiers);
        return true;
    }

    @Override
    public void renderTextHover(MatrixStack matrices, @Nullable Style textStyle, int x, int y) {
        renderTextHoverEffect(matrices, textStyle, x, y);
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
