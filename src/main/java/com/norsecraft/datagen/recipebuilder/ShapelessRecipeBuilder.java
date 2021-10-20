package com.norsecraft.datagen.recipebuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.norsecraft.common.util.exception.InvalidRecipeArgumentException;
import com.norsecraft.datagen.RecipeDataGenerator;
import net.minecraft.item.ItemConvertible;

import java.util.Arrays;
import java.util.List;

public class ShapelessRecipeBuilder {

    private String group = "";
    private List<ItemConvertible> ingredients;
    private ItemConvertible output;
    private int outputCount = 1;
    private final String recipeName;

    public ShapelessRecipeBuilder(String recipeName) {
        this.recipeName = recipeName;
    }

    /**
     * Set the recipe group.
     *
     * @param group the group name
     * @return the shapeless builder
     */
    public ShapelessRecipeBuilder group(String group) {
        this.group = group;
        return this;
    }

    /**
     * Add the ingredients
     *
     * @param ingredients the ingredients for the recipe
     * @return the shapeless builder
     */
    public ShapelessRecipeBuilder addIngredients(ItemConvertible... ingredients) {
        this.ingredients = Arrays.asList(ingredients);
        return this;
    }

    /**
     * Set the output item
     *
     * @param output the output item
     * @return the shapeless builder
     */
    public ShapelessRecipeBuilder output(ItemConvertible output) {
        this.output = output;
        return this;
    }

    /**
     * Set the output count
     *
     * @param count the output count
     * @return the shapeless builder
     */
    public ShapelessRecipeBuilder outputCount(int count) {
        this.outputCount = count;
        return this;
    }

    /**
     * This method builds the recipe, and saves them into the file
     */
    public void build() {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonObject main = new JsonObject();
        String name = "crafting_shapeless";
        main.addProperty("type", String.format("minecraft:%s", name));
        if (!group.isEmpty()) {
            main.addProperty("group", group);
        }
        JsonArray array = new JsonArray();
        if (ingredients == null || ingredients.isEmpty())
            throw new InvalidRecipeArgumentException("Could not create recipe: {}, ingredients list is null", recipeName);

        for (ItemConvertible item : this.ingredients) {
            array.add(RecipeDataGenerator.getItemName(item));
        }
        main.add("ingredients", array);
        if (output == null)
            throw new InvalidRecipeArgumentException("Could not create recipe: {}, output item is null", recipeName);

        JsonObject result = new JsonObject();

        result.addProperty("item", RecipeDataGenerator.getItemName(output));
        result.addProperty("count", outputCount);
        main.add("result", result);

        RecipeDataGenerator.save(gson.toJson(main), recipeName);
    }

}
