package com.norsecraft.client.ymir.interpretation;

import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;

public interface RecipeGuiInterpretation<C extends Inventory> {

    void populateRecipeFinder(RecipeMatcher finder);

    void clearCraftingSlots();

    boolean matches(Recipe<? super C> recipe);

    int getCraftingResultSlotIndex();

    int getCraftingWidth();

    int getCraftingHeight();

    int getCraftingSlotCount();

    RecipeBookCategory getCategory();

    boolean canInsertIntoSot(int slot);

}
