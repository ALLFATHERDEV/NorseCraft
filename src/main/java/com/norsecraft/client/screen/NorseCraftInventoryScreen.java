package com.norsecraft.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.render.TextureSprite;
import com.norsecraft.client.screen.widget.ImageButton;
import com.norsecraft.common.screenhandler.NorseCraftInventoryScreenHandler;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class NorseCraftInventoryScreen extends AbstractInventoryScreen<NorseCraftInventoryScreenHandler> implements RecipeBookProvider {

    private static final Identifier TEXTURE = NorseCraftMod.ncTex("gui/inventory.png");
    private float mouseX;
    private float mouseY;
    private final RecipeBookWidget recipeBook = new RecipeBookWidget();
    private boolean narrow;
    private static final TextureSprite[] BUTTON_SPRITES = new TextureSprite[]{
            new TextureSprite(177, 0, 17, 16),
            new TextureSprite(194, 0, 17, 17),
            new TextureSprite(212, 0, 17, 17)
    };
    private static final TextureSprite[] BRIGHTER_BUTTON_SPRITES =  new TextureSprite[]{
            new TextureSprite(177, 18, 17, 16),
            new TextureSprite(194, 18, 17, 17),
            new TextureSprite(212, 18, 17, 17)
    };

    public NorseCraftInventoryScreen(NorseCraftInventoryScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
        this.passEvents = true;
    }

    public void handledScreenTick() {
        if (this.client.interactionManager.hasCreativeInventory()) {
            this.client.setScreen(new CreativeInventoryScreen(this.client.player));
        } else {
            this.recipeBook.update();
        }
    }

    @Override
    protected void init() {
        if (this.client.interactionManager.hasCreativeInventory()) {
            this.client.setScreen(new CreativeInventoryScreen(this.client.player));
        } else {
            super.init();
            this.narrow = this.width < 379;
            this.recipeBook.initialize(this.width, this.height, this.client, this.narrow, this.handler);

            this.addSelectableChild(this.recipeBook);
            this.setInitialFocus(this.recipeBook);

            int i = (this.width - this.backgroundWidth) / 2;
            int j = (this.height - this.backgroundHeight) / 2;

            for(int l = 0; l < 3; l++) {
                this.addDrawableChild(new ImageButton(i + 116 + l * 18, j + 62, 17, 17, LiteralText.EMPTY, TEXTURE, BRIGHTER_BUTTON_SPRITES[l], BUTTON_SPRITES[l],  false, ACTIONS[l]));
            }
        }
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.recipeBook.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
        this.recipeBook.drawGhostSlots(matrices, this.x, this.y, false, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
        this.recipeBook.drawTooltip(matrices, this.x, this.y, mouseX, mouseY);
        this.mouseX = (float) mouseX;
        this.mouseY = (float) mouseY;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = this.x;
        int j = this.y;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        InventoryScreen.drawEntity(i + 51, j + 75, 30, (float) (i + 51) - this.mouseX, (float) (j + 75 - 50) - this.mouseY, this.client.player);
    }

    @Override
    protected boolean isPointWithinBounds(int x, int y, int width, int height, double pointX, double pointY) {
        return (!this.narrow || !this.recipeBook.isOpen()) && super.isPointWithinBounds(x, y, width, height, pointX, pointY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.recipeBook.mouseClicked(mouseX, mouseY, button)) {
            this.setFocused(this.recipeBook);
            return true;
        } else {
            return (!this.narrow || !this.recipeBook.isOpen()) && super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
        boolean bl = mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + this.backgroundWidth) || mouseY >= (double)(top + this.backgroundHeight);
        return this.recipeBook.isClickOutsideBounds(mouseX, mouseY, this.x, this.y, this.backgroundWidth, this.backgroundHeight, button) && bl;
    }

    @Override
    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {
        super.onMouseClick(slot, slotId, button, actionType);
        this.recipeBook.slotClicked(slot);
    }

    @Override
    public void refreshRecipeBook() {
        this.recipeBook.refresh();
    }

    @Override
    public RecipeBookWidget getRecipeBookWidget() {
        return this.recipeBook;
    }

    private static final ButtonWidget.PressAction[] ACTIONS = new ButtonWidget.PressAction[] {
            (button) -> {
                NorseCraftMod.LOGGER.info("Factions");
            },
            (button) -> {
                NorseCraftMod.LOGGER.info("Reputation");
            },
            (button) -> {
                NorseCraftMod.LOGGER.info("Quests");
            }
    };

}
