package com.norsecraft.common.recipe.campfire;

import com.google.gson.JsonObject;
import com.norsecraft.common.recipe.NCRecipeItem;
import com.norsecraft.common.recipe.NorseCraftRecipeSerializer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class CampfireRecipeSerializer extends NorseCraftRecipeSerializer<CampfireRecipe> {

    private final RecipeFactory recipeFactory;

    public CampfireRecipeSerializer(RecipeFactory factory) {
        this.recipeFactory = factory;
    }

    @Override
    public CampfireRecipe readFromJson(Identifier id, JsonObject json) {
        NCRecipeItem input = NCRecipeItem.of(json.get("ingredient").getAsJsonObject());
        NCRecipeItem output = NCRecipeItem.of(json.get("result").getAsJsonObject());
        boolean withBowl = json.get("with-bowl").getAsBoolean();
        return this.recipeFactory.create(id, input, output, withBowl);
    }

    @Override
    public CampfireRecipe readFromServer(Identifier id, PacketByteBuf buf) {
        NCRecipeItem input = NCRecipeItem.of(buf);
        NCRecipeItem output = NCRecipeItem.of(buf.readItemStack());
        boolean withBowl = buf.readBoolean();
        return this.recipeFactory.create(id, input, output, withBowl);
    }

    @Override
    public void writeToServer(PacketByteBuf buf, CampfireRecipe recipe) {
        recipe.getInput().netWrite(buf);
        buf.writeItemStack(recipe.getOutput());
        buf.writeBoolean(recipe.isWithBowl());
    }

    public interface RecipeFactory {

        CampfireRecipe create(Identifier id, NCRecipeItem input, NCRecipeItem output, boolean withBowl);

    }
}
