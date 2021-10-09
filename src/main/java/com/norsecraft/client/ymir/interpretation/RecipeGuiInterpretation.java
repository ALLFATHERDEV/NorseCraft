package com.norsecraft.client.ymir.interpretation;

import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;

/**
 * Use this class if you have a recipe gui
 *
 * @param <C> the recipe inventory
 */
public interface RecipeGuiInterpretation<C extends Inventory> {

    /**
     * This method checks the registered recipes for the correct recipe
     *
     * @param finder the recipe matcher from mc
     */
    void populateRecipeFinder(RecipeMatcher finder);

    /**
     * This method clears all the crafting slots
     */
    void clearCraftingSlots();

    /**
     * This method looks if the recipe matches with the recipe from the gui
     * @param recipe the recipe to check
     * @return true if it matches or false if not
     */
    boolean matches(Recipe<? super C> recipe);

    /**
     * @return tje slot index for the crafting result
     */
    int getCraftingResultSlotIndex();

    /**
     * @return the crafting grid width
     */
    int getCraftingWidth();

    /**
     * @return the crafting grid height
     */
    int getCraftingHeight();

    /**
     * @return the crafting grid slot count
     */
    int getCraftingSlotCount();

    /**
     * @return the category for the recipe book
     */
    RecipeBookCategory getCategory();

    /**
     * @param slot the slot to check
     * @return true if you can insert into the slot or false if not
     */
    boolean canInsertIntoSot(int slot);

}
