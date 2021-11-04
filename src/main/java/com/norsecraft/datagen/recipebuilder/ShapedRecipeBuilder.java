package com.norsecraft.datagen.recipebuilder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.datagen.RecipeDataGenerator;
import net.minecraft.item.ItemConvertible;

import java.util.List;
import java.util.Map;

public class ShapedRecipeBuilder {

    private String group = "";
    private final String recipeName;
    private ItemConvertible output;
    private int outputCount = 1;
    private String[] shape;
    private final Map<Character, ItemConvertible> ingredientsMap = Maps.newHashMap();

    public ShapedRecipeBuilder(String recipeName) {
        this.recipeName = recipeName;
    }

    /**
     * Set the shape here.
     * The shape structure:
     * <p>
     * "ABC"
     * "DEF"
     * "GHI"
     * <p>
     * This is the shape structure. Every letter can be a item, so in this recipe we have 9 different items for the recipe.
     * <p>
     * In this recipe we have only 4 letters... so 4 different items:
     * <p>
     * "AAA"
     * "BCB"
     * "ADA"
     * <p>
     * The shape array have to be a length of 3 otherwise it will throw an error
     * Every shape string have to be the length of 3 letters. If you want space between some items, just use the "space" key: ' '
     *
     * @param shape the shape
     * @return the shaped builder
     */
    public ShapedRecipeBuilder shape(String... shape) {
        this.shape = shape;
        return this;
    }

    /**
     * In this method you have to set the ingredients now, so the recipe now which character is which item
     * <p>
     * Here the example for the second shape (from the "shape" method)
     * <p>
     * setIngredient('A', Items.APPLE).
     * setIngredient('B', Items.BOW).
     * setIngredient('C', Items.CLAY).
     * setIngredient('D', Items.IRON_INGOT)
     * <p>
     * So now the builder class knows, which char has which item
     *
     * @param c    the character from the shape
     * @param item the item to the character
     * @return the shaped builder
     */
    public ShapedRecipeBuilder setIngredient(Character c, ItemConvertible item) {
        if (item == null) {
            NorseCraftMod.LOGGER.warn("Ingredient item for character {} is null. Recipe name: {}", c, recipeName);
        }
        if (this.ingredientsMap.containsKey(c)) {
            NorseCraftMod.LOGGER.warn("Tried to add a duplicated character for recipe {}", recipeName);
            return this;
        }
        this.ingredientsMap.put(c, item);
        return this;
    }

    /**
     * Set the output item
     *
     * @param output the output item
     * @return the shaped builder
     */
    public ShapedRecipeBuilder output(ItemConvertible output) {
        this.output = output;
        return this;
    }

    /**
     * Set the output count
     *
     * @param outputCount the output count
     * @return the shaped builder
     */
    public ShapedRecipeBuilder outputCount(int outputCount) {
        this.outputCount = outputCount;
        return this;
    }

    /**
     * Set the recipe group
     *
     * @param group the group name
     * @return the shaped builder
     */
    public ShapedRecipeBuilder group(String group) {
        this.group = group;
        return this;
    }


    /**
     * Builds the recipe and saves it into the file
     */
    public void build() {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonObject main = new JsonObject();
        String name = "crafting_shaped";
        main.addProperty("type", String.format("minecraft:%s", name));
        if (!group.isEmpty())
            main.addProperty("group", group);
        JsonArray pattern = new JsonArray();
        if (this.shape.length != 3)
            throw new IllegalStateException("Crafting shape array greater or less then 3");

        List<Character> chars = Lists.newArrayList();
        for (String s : shape) {
            int length = s.length();
            if (length != 3)
                throw new IllegalStateException("The shape " + s + " has not the size of 3!");
            for (int i = 0; i < 3; i++) {
                char c = s.charAt(i);
                if (!chars.contains(c))
                    chars.add(c);
            }
            pattern.add(s);
        }

        JsonObject key = new JsonObject();
        for (Map.Entry<Character, ItemConvertible> entry : this.ingredientsMap.entrySet()) {
            char c = entry.getKey();
            if (!chars.contains(c))
                throw new IllegalStateException("The character " + c + " does not exist in the crafting shape");
            JsonObject item = new JsonObject();
            item.addProperty("item", RecipeDataGenerator.getItemName(entry.getValue()));
            key.add(Character.toString(c), item);
        }

        JsonObject result = new JsonObject();
        if (output == null)
            throw new NullPointerException("Output item for recipe " + recipeName + " is null");
        result.addProperty("item", RecipeDataGenerator.getItemName(output));
        result.addProperty("count", outputCount);

        main.add("pattern", pattern);
        main.add("key", key);
        main.add("result", result);

        RecipeDataGenerator.save(gson.toJson(main), recipeName);
    }

}
