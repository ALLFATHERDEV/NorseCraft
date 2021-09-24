package com.norsecraft.client.ymir.screen;

import com.norsecraft.client.ymir.YmirScreenDrawing;
import com.norsecraft.client.ymir.interpretation.GuiInterpretation;
import com.norsecraft.client.ymir.widget.YmirPanel;
import com.norsecraft.client.ymir.widget.YmirWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class YmirClientScreen extends Screen implements YmirScreenImpl {

    protected GuiInterpretation interpretation;
    protected int left = 0;
    protected int top = 0;

    protected int titleX;
    protected int titleY;

    @Nullable
    protected YmirWidget lastResponder = null;

    private final MouseInputHandler<YmirClientScreen> mouseInputHandler = new MouseInputHandler<>(this);

    public YmirClientScreen(GuiInterpretation interpretation) {
        this(new LiteralText(""), interpretation);
    }

    public YmirClientScreen(Text title, GuiInterpretation interpretation) {
        super(title);
        this.interpretation = interpretation;
        interpretation.getRootPanel().validate(interpretation);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public GuiInterpretation getInterpretation() {
        return this.interpretation;
    }

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

    @Override
    public void removed() {
        super.removed();
        this.client.keyboard.setRepeatEvents(false);
    }

    @Override
    public @Nullable YmirWidget getLastResponder() {
        return this.lastResponder;
    }

    @Override
    public void setLastResponder(@Nullable YmirWidget lastResponder) {
        this.lastResponder = lastResponder;
    }

    protected void reposition(int screenWidth, int screenHeight) {
        if (interpretation != null) {
            YmirPanel root = interpretation.getRootPanel();
            if (root != null) {
                titleX = interpretation.getTitlePos().x();
                titleY = interpretation.getTitlePos().y();

                if (!interpretation.isFullscreen()) {
                    this.left = ((screenWidth - root.getWidth()) / 2) - (root.getWidth() * 2);
                    this.top = ((screenHeight - root.getHeight()) / 2) - (root.getHeight() * 2);
                } else {
                    this.left = 0;
                    this.top = 0;

                    root.setSize(screenWidth, screenHeight);
                }
            }
        }
    }

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

    }

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
