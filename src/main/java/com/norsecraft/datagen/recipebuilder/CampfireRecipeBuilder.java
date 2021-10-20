package com.norsecraft.datagen.recipebuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.norsecraft.common.recipe.campfire.CampfireRecipe;
import com.norsecraft.common.recipe.NCRecipeItem;
import com.norsecraft.datagen.RecipeDataGenerator;
import org.apache.logging.log4j.message.ParameterizedMessage;

public class CampfireRecipeBuilder {

    private final String recipeName;
    private NCRecipeItem input;
    private NCRecipeItem output;
    private boolean withBowl = false;

    public CampfireRecipeBuilder(String recipeName) {
        this.recipeName = recipeName;
    }

    /**
     * @param input input item
     * @return the builder
     */
    public CampfireRecipeBuilder input(NCRecipeItem input) {
        this.input = input;
        return this;
    }

    /**
     * @param output output item
     * @return the builder
     */
    public CampfireRecipeBuilder output(NCRecipeItem output) {
        this.output = output;
        return this;
    }

    /**
     * @return true if it is a bowl recipe otherwise false
     */
    public CampfireRecipeBuilder withBowl() {
        this.withBowl = true;
        return this;
    }

    /**
     * Build it
     */
    public void build() {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonObject main = new JsonObject();
        main.addProperty("type", CampfireRecipe.TYPE.toString());
        JsonObject ingredient = new JsonObject();
        if (this.input == null)
            throw new NullPointerException(ParameterizedMessage.format("Could not create recipe {}. You have to set a input item!", new Object[]{this.recipeName}));
        this.input.write(ingredient);
        if (this.output == null) {
            throw new NullPointerException(ParameterizedMessage.format("Could not create recipe {}. You have to set a output item!", new Object[]{this.recipeName}));
        }
        JsonObject result = new JsonObject();
        this.output.write(result);
        main.add("ingredient", ingredient);
        main.add("result", result);
        main.addProperty("with-bowl", withBowl);

        RecipeDataGenerator.save(gson.toJson(main), recipeName);
    }

}
