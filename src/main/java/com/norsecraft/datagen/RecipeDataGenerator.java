package com.norsecraft.datagen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.util.exception.InvalidRecipeArgumentException;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RecipeDataGenerator implements NorseCraftDataGenerator {

    @Override
    public void generate() {
        this.shapedRecipe("test").shape(
                        "AAA",
                        " B ",
                        "CBC")
                .setIngredient('A', Items.APPLE)
                .setIngredient('B', Items.IRON_INGOT)
                .setIngredient('C', Items.DIAMOND)
                .output(Items.ACACIA_BOAT)
                .outputCount(3).build();
    }

    /**
     * Use this method to build a shaped recipe
     *
     * @param recipeName the recipe name
     * @return a shaped builder
     */
    private ShapedBuilder shapedRecipe(String recipeName) {
        return new ShapedBuilder(recipeName);
    }

    /**
     * Use this method to build a shapeless recipe
     *
     * @param recipeName the recipe name
     * @return a shapeless builder
     */
    private ShapelessBuilder shapelessRecipe(String recipeName) {
        return new ShapelessBuilder(recipeName);
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

    private void save(String jsonString, String recipeName) {
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

    private String getItemName(ItemConvertible item) {
        return Registry.ITEM.getId(item.asItem()).toString();
    }

    /**
     * Helps you to build a shapeless recipe
     */
    private class ShapelessBuilder {

        private String group = "";
        private List<ItemConvertible> ingredients;
        private ItemConvertible output;
        private int outputCount = 1;
        private final String recipeName;

        ShapelessBuilder(String recipeName) {
            this.recipeName = recipeName;
        }

        /**
         * Set the recipe group.
         *
         * @param group the group name
         * @return the shapeless builder
         */
        public ShapelessBuilder group(String group) {
            this.group = group;
            return this;
        }

        /**
         * Add the ingredients
         *
         * @param ingredients the ingredients for the recipe
         * @return the shapeless builder
         */
        public ShapelessBuilder addIngredients(ItemConvertible... ingredients) {
            this.ingredients = Arrays.asList(ingredients);
            return this;
        }

        /**
         * Set the output item
         *
         * @param output the output item
         * @return the shapeless builder
         */
        public ShapelessBuilder output(ItemConvertible output) {
            this.output = output;
            return this;
        }

        /**
         * Set the output count
         *
         * @param count the output count
         * @return the shapeless builder
         */
        public ShapelessBuilder outputCount(int count) {
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
                array.add(getItemName(item));
            }
            main.add("ingredients", array);
            if (output == null)
                throw new InvalidRecipeArgumentException("Could not create recipe: {}, output item is null", recipeName);

            JsonObject result = new JsonObject();

            result.addProperty("item", getItemName(output));
            result.addProperty("count", outputCount);
            main.add("result", result);

            save(gson.toJson(main), recipeName);
        }

    }

    /**
     * Builder for shaped recipe types
     */
    private class ShapedBuilder {

        private String group = "";
        private final String recipeName;
        private ItemConvertible output;
        private int outputCount = 1;
        private String[] shape;
        private final Map<Character, ItemConvertible> ingredientsMap = Maps.newHashMap();

        ShapedBuilder(String recipeName) {
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
        public ShapedBuilder shape(String... shape) {
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
        public ShapedBuilder setIngredient(Character c, ItemConvertible item) {
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
        public ShapedBuilder output(ItemConvertible output) {
            this.output = output;
            return this;
        }

        /**
         * Set the output count
         *
         * @param outputCount the output count
         * @return the shaped builder
         */
        public ShapedBuilder outputCount(int outputCount) {
            this.outputCount = outputCount;
            return this;
        }

        /**
         * Set the recipe group
         *
         * @param group the group name
         * @return the shaped builder
         */
        public ShapedBuilder group(String group) {
            this.group = group;
            return this;
        }


        /**
         * Builds the recipe and saves it into the file
         */
        public void build() {
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            JsonObject main = new JsonObject();
            String name = "crafting_shapeless";
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
                item.addProperty("item", getItemName(entry.getValue()));
                key.add(Character.toString(c), item);
            }

            JsonObject result = new JsonObject();
            if (output == null)
                throw new NullPointerException("Output item for recipe " + recipeName + " is null");
            result.addProperty("item", getItemName(output));
            result.addProperty("count", outputCount);

            main.add("pattern", pattern);
            main.add("key", key);
            main.add("result", result);

            save(gson.toJson(main), recipeName);
        }

    }

}
