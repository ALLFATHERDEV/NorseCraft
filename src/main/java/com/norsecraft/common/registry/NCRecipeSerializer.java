package com.norsecraft.common.registry;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.recipe.campfire.CampfireRecipe;
import com.norsecraft.common.recipe.campfire.CampfireRecipeSerializer;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.registry.Registry;

/**
 * Register the recipe serializer from the mod
 */
public class NCRecipeSerializer {

    public static RecipeSerializer<CampfireRecipe> campfireSerializer;

    public static void register() {
        campfireSerializer = registerSerializer("campfire", new CampfireRecipeSerializer(CampfireRecipe::new));
    }

    private static <T extends Recipe<?>> RecipeSerializer<T> registerSerializer(String name, RecipeSerializer<T> serializer) {
        return Registry.register(Registry.RECIPE_SERIALIZER, NorseCraftMod.nc(name), serializer);
    }

}
