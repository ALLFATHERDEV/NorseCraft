package com.norsecraft.client.ymir.screen;

import com.norsecraft.client.ymir.YmirScreenDrawing;
import com.norsecraft.client.ymir.interpretation.GuiInterpretation;
import com.norsecraft.client.ymir.interpretation.SyncedGuiInterpretation;
import com.norsecraft.client.ymir.widget.YmirPanel;
import com.norsecraft.client.ymir.widget.YmirWidget;
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

public class YmirInventoryScreen<T extends SyncedGuiInterpretation> extends HandledScreen<T> implements YmirScreenImpl {

    protected SyncedGuiInterpretation interpretation;
    @Nullable
    protected YmirWidget lastResponder = null;
    private final MouseInputHandler<YmirInventoryScreen<T>> mouseInputHandler = new MouseInputHandler<>(this);

    public YmirInventoryScreen(T interpretation, PlayerEntity player) {
        this(interpretation, player, new LiteralText(""));
    }

    public YmirInventoryScreen(T interpretation, PlayerEntity player, Text title) {
        super(interpretation, player.getInventory(), title);
        this.interpretation = interpretation;
        width = 18 * 9;
        height = 18 * 9;
        this.backgroundWidth = 18 * 9;
        this.backgroundHeight = 18 * 9;
        interpretation.getRootPanel().validate(interpretation);
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
    public GuiInterpretation getInterpretation() {
        return this.interpretation;
    }

    @Override
    public @Nullable YmirWidget getLastResponder() {
        return this.lastResponder;
    }

    @Override
    public void setLastResponder(@Nullable YmirWidget lastResponder) {
        this.lastResponder = lastResponder;
    }

    private void clearPeers() {
        this.interpretation.slots.clear();
    }

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

    @Override
    public boolean isPauseScreen() {
        return false;
    }

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

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {

    }

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

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        if (interpretation != null && interpretation.isTitleVisible()) {
            int width = interpretation.getRootPanel().getWidth();
            YmirScreenDrawing.drawString(matrices, getTitle().asOrderedText(), interpretation.getTitleAlignment(), titleX, titleY, width, interpretation.getTitleColor());
        }
    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        if(interpretation != null) {
            YmirPanel root = interpretation.getRootPanel();
            if(root != null)
                root.tick();
        }
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        if(interpretation != null)
            interpretation.cycleFocus(lookForwards);
        return true;
    }

    @Override
    protected void addElementNarrations(NarrationMessageBuilder builder) {
        if(interpretation != null)
            NarrationHelper.addNarrations(interpretation.getRootPanel(), builder);
    }
}
