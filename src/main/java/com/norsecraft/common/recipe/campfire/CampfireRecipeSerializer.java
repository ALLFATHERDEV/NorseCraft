package com.norsecraft.common.recipe.campfire;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.norsecraft.common.recipe.NCRecipeItem;
import com.norsecraft.common.recipe.NorseCraftRecipeSerializer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CampfireRecipeSerializer extends NorseCraftRecipeSerializer<CampfireRecipe> {

    private final RecipeFactory recipeFactory;

    public CampfireRecipeSerializer(RecipeFactory factory) {
        this.recipeFactory = factory;
    }

    @Override
    public CampfireRecipe readFromJson(Identifier id, JsonObject json) {
        NCRecipeItem input = NCRecipeItem.of(json.get("ingredient").getAsJsonObject());
        JsonElement res = json.get("result");
        NCRecipeItem output;
        if(res.isJsonObject())
            output = NCRecipeItem.of(json.get("result").getAsJsonObject());
        else {
            String s = json.get("result").getAsString();
            output = NCRecipeItem.of(Registry.ITEM.get(new Identifier(s.split(":")[0], s.split(":")[1])));
        }
        boolean withBowl = false;
        if(json.has("with-bowl"))
            withBowl = json.get("with-bowl").getAsBoolean();
        int cookTime = 200;
        if(json.has("cook-time"))
            cookTime = json.get("cook-time").getAsInt();

        return this.recipeFactory.create(id, input, output, withBowl, cookTime);
    }

    @Override
    public CampfireRecipe readFromServer(Identifier id, PacketByteBuf buf) {
        NCRecipeItem input = NCRecipeItem.of(buf);
        NCRecipeItem output = NCRecipeItem.of(buf.readItemStack());
        boolean withBowl = buf.readBoolean();
        return this.recipeFactory.create(id, input, output, withBowl, buf.readInt());
    }

    @Override
    public void writeToServer(PacketByteBuf buf, CampfireRecipe recipe) {
        recipe.getInput().netWrite(buf);
        buf.writeItemStack(recipe.getOutput());
        buf.writeBoolean(recipe.isWithBowl());
        if(recipe.getCookTime() <= 0)
            buf.writeInt(200);
        else
            buf.writeInt(recipe.getCookTime());
    }

    public interface RecipeFactory {

        CampfireRecipe create(Identifier id, NCRecipeItem input, NCRecipeItem output, boolean withBowl, int cookTime);

    }
}
