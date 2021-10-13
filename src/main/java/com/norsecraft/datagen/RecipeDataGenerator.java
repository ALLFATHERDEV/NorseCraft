package com.norsecraft.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.norsecraft.common.util.exception.InvalidRecipeArgumentException;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RecipeDataGenerator implements NorseCraftDataGenerator {

    @Override
    public void generate() {

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

    private class ShapelessBuidler {

        private final String name = "crafting_shapeless";
        private String group = "";
        private List<ItemConvertible> ingredients;
        private ItemConvertible output;
        private int outputCount = 1;
        private final String recipeName;

        ShapelessBuidler(String recipeName) {
            this.recipeName = recipeName;
        }

        public ShapelessBuidler group(String group) {
            this.group = group;
            return this;
        }

        public ShapelessBuidler addIngredients(ItemConvertible... ingredients) {
            this.ingredients = Arrays.asList(ingredients);
            return this;
        }

        public ShapelessBuidler output(ItemConvertible output) {
            this.output = output;
            return this;
        }

        public ShapelessBuidler outputCount(int count) {
            this.outputCount = count;
            return this;
        }

        public void build() {
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            JsonObject main = new JsonObject();
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

}
