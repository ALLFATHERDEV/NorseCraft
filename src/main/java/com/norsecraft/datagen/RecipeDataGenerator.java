package com.norsecraft.datagen;

import com.norsecraft.datagen.recipebuilder.CampfireRecipeBuilder;
import com.norsecraft.datagen.recipebuilder.ShapedRecipeBuilder;
import com.norsecraft.datagen.recipebuilder.ShapelessRecipeBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RecipeDataGenerator implements NorseCraftDataGenerator {

    @Override
    public void generate() {

    }

    private CampfireRecipeBuilder campfireRecipe(String recipeName) {
        return new CampfireRecipeBuilder(recipeName);
    }

    /**
     * Use this method to build a shaped recipe
     *
     * @param recipeName the recipe name
     * @return a shaped builder
     */
    private ShapedRecipeBuilder shapedRecipe(String recipeName) {
        return new ShapedRecipeBuilder(recipeName);
    }

    /**
     * Use this method to build a shapeless recipe
     *
     * @param recipeName the recipe name
     * @return a shapeless builder
     */
    private ShapelessRecipeBuilder shapelessRecipe(String recipeName) {
        return new ShapelessRecipeBuilder(recipeName);
    }

    /**
     * This method creates an item, and set the nbt tag for the recipe
     *
     * @param itemStack the item
     * @param nbt       the nbt compound
     * @return the item with the nbt compound set
     */
    private ItemStack withNbt(ItemStack itemStack, NbtCompound nbt) {
        if (itemStack == null)
            throw new NullPointerException("Could not create item with nbt, itemstack is null");
        itemStack.setNbt(nbt);
        return itemStack;
    }

    public static void save(String jsonString, String recipeName) {
        try {
            File folder = new File("../data_generator/recipes/");
            if (!folder.exists())
                if (!folder.mkdirs())
                    return;
            File file = new File(folder, recipeName + ".json");
            FileWriter writer = new FileWriter(file);
            writer.write(jsonString);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDataGeneratorName() {
        return "Recipe Generator";
    }

    public static String getItemName(ItemConvertible item) {
        return Registry.ITEM.getId(item.asItem()).toString();
    }


}
