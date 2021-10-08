package com.norsecraft.common.registry;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.recipe.CampfireRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;

public class NCRecipeTypes {

    public static RecipeType<CampfireRecipe> campfire;

    public static void register() {
        campfire = registerRecipe("campfire", new RecipeType<>() {
            @Override
            public String toString() {
                return "campfire";
            }
        });
    }

    private static <T extends Recipe<?>> RecipeType<T> registerRecipe(String name, RecipeType<T> recipeType) {
        return Registry.register(Registry.RECIPE_TYPE, NorseCraftMod.nc(name), recipeType);
    }

}
