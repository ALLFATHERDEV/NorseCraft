package com.norsecraft.common.registry;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.recipe.CampfireRecipe;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.registry.Registry;

public class NCRecipeSerializer {

    public static RecipeSerializer<CampfireRecipe> campfireSerializer;

    public static void register() {
        campfireSerializer = registerSerializer("campfire", new CookingRecipeSerializer<>(CampfireRecipe::new, 200));
    }

    private static <T extends Recipe<?>> RecipeSerializer<T> registerSerializer(String name, RecipeSerializer<T> serializer) {
        return Registry.register(Registry.RECIPE_SERIALIZER, NorseCraftMod.nc(name), serializer);
    }

}
