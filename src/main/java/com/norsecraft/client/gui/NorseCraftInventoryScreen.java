package com.norsecraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.render.TextureSprite;
import com.norsecraft.client.ymir.YmirScreenDrawing;
import com.norsecraft.client.ymir.widget.data.Texture;
import com.norsecraft.common.screenhandler.NorseCraftInventoryScreenHandler;
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

import javax.swing.plaf.TextUI;

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
    private static final TextureSprite[] BRIGHTER_BUTTON_SPRITES = new TextureSprite[]{
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

            //Levels
            Texture texture1 = Texture.component(34 ,180, 35, 36);
            Texture texture1Hovered = Texture.component(0, 180, 35, 36);
            this.addDrawableChild(new Button(i + 151, j + 61, 18, 18, texture1, texture1Hovered, (button) -> {
                NorseCraftMod.LOGGER.info("Level up");
            }));

            //Reputations
            Texture texture2 = Texture.component(34, 144, 35, 36);
            Texture texture2Hovered = Texture.component(0, 144, 35, 36);
            this.addDrawableChild(new Button(i + 115, j + 61, 18, 18, texture2, texture2Hovered, (button) -> {
                NorseCraftMod.LOGGER.info("Reputation");
            }));

            //Factions
            Texture texture3 = Texture.component(34, 108, 35, 36);
            Texture texture3Hovered = Texture.component(0, 108, 35, 36);
            this.addDrawableChild(new Button(i + 97, j + 61, 18, 18, texture3, texture3Hovered, (button) -> {
                NorseCraftMod.LOGGER.info("Factions");
            }));

            //Quests
            Texture texture4 = Texture.component(34, 72, 35, 36);
            Texture texture4Hovered = Texture.component(0, 72, 35, 36);
            this.addDrawableChild(new Button(i + 133, j + 61, 18, 18, texture4, texture4Hovered, (button) -> {
                NorseCraftMod.LOGGER.info("Quests");
            }));
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
        if (this.recipeBook.mouseClicked(mouseX, mouseY, button)) {
            this.setFocused(this.recipeBook);
            return true;
        } else {
            return (!this.narrow || !this.recipeBook.isOpen()) && super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
        boolean bl = mouseX < (double) left || mouseY < (double) top || mouseX >= (double) (left + this.backgroundWidth) || mouseY >= (double) (top + this.backgroundHeight);
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

    public static class Button extends ButtonWidget {

        private final Texture texture;
        private final Texture hovered;

        public Button(int x, int y, int width, int height, Texture texture, Texture hovered, PressAction onPress) {
            super(x, y, width, height, LiteralText.EMPTY, onPress);
            this.texture = texture;
            this.hovered = hovered;
        }


        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            if (isHovered() && hovered != null) {
                YmirScreenDrawing.texturedGuiRect(matrices, x, y, hovered);
            } else {
                YmirScreenDrawing.texturedGuiRect(matrices, x, y, texture);
            }
        }
    }

}
