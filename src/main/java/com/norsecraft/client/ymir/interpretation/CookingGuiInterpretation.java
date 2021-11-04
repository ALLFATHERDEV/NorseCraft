package com.norsecraft.client.ymir.interpretation;

import com.norsecraft.client.ymir.screen.BackgroundPainter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.world.World;

/**
 * Abstract class for cooking recipe guis, like the {@link com.norsecraft.common.gui.CampfireGuiInterpretation}
 */
public abstract class CookingGuiInterpretation extends SyncedGuiInterpretation implements RecipeGuiInterpretation<Inventory> {

    /**
     * The world reference
     */
    protected final World world;

    /**
     * The category
     */
    private final RecipeBookCategory category;

    /**
     * This is the default constructor
     *
     * @param type             the registered screen type
     * @param category         the recipe book category
     * @param syncId           the window id. Will be provided from mc
     * @param playerInventory  the player inventory reference
     * @param ctx              the screen handler context with the block pos and the world
     * @param propertyDelegate the integer data to sync
     * @param painter          the background painter
     */
    public CookingGuiInterpretation(ScreenHandlerType<?> type,
                                    RecipeBookCategory category, int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx,
                                    PropertyDelegate propertyDelegate, BackgroundPainter painter, int size) {
        super(type, syncId, playerInventory, getBlockInventory(ctx, size), propertyDelegate, painter);
        this.category = category;
        checkDataCount(propertyDelegate, 4);
        this.world = playerInventory.player.world;
    }

    public void populateRecipeFinder(RecipeMatcher finder) {
        if (this.blockInventory instanceof RecipeInputProvider) {
            ((RecipeInputProvider) this.blockInventory).provideRecipeInputs(finder);
        }
    }

    public abstract void clearCraftingSlots();

    @Override
    public boolean matches(Recipe<? super Inventory> recipe) {
        return recipe.matches(this.blockInventory, this.world);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.blockInventory.canPlayerUse(player);
    }

    @Override
    public RecipeBookCategory getCategory() {
        return this.category;
    }

    @Override
    public boolean canInsertIntoSot(int slot) {
        return slot != 1;
    }
}
