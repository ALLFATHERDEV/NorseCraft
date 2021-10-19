package com.norsecraft.common.recipe;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.registry.NCRecipeTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public class CampfireRecipe extends AbstractCookingRecipe {

    public static final String TYPE = NorseCraftMod.nc("campfire").toString();

    public CampfireRecipe(Identifier id, String group, Ingredient input, ItemStack output, float experience, int cookTime) {
        super(NCRecipeTypes.campfire, id, group, input, output, experience, cookTime);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.CAMPFIRE_COOKING;
    }
}
