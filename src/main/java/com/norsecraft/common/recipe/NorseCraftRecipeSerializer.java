package com.norsecraft.common.recipe;

import com.google.gson.JsonObject;
import com.norsecraft.common.util.nbt.NbtDeserializer;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

/**
 * This is a base class for all our custom recipe serializer, bcs this class has NBT support for items
 *
 * @param <T> the recipe type
 */
public abstract class NorseCraftRecipeSerializer<T extends Recipe<? extends Inventory>> implements RecipeSerializer<T> {

    /**
     * A helper class for deserializing nbt compounds if needed
     */
    protected final NbtDeserializer nbtDeserializer = new NbtDeserializer();

    /**
     * This method reads the recipe from the json file
     *
     * @param id   the id from the recipe
     * @param json the json object
     * @return the recipe
     */
    public abstract T readFromJson(Identifier id, JsonObject json);

    /**
     * This method reads the recipe from the server
     *
     * @param id  the id from the recipe
     * @param buf the byte buf with the recipe data
     * @return the recipe
     */
    public abstract T readFromServer(Identifier id, PacketByteBuf buf);

    /**
     * This method is to write all the recipe data to the server
     *
     * @param buf    the byte buf for the data
     * @param recipe the recipe to write
     */
    public abstract void writeToServer(PacketByteBuf buf, T recipe);


    @Override
    public T read(Identifier id, JsonObject json) {
        return this.readFromJson(id, json);
    }

    @Override
    public T read(Identifier id, PacketByteBuf buf) {
        return this.readFromServer(id, buf);
    }

    @Override
    public void write(PacketByteBuf buf, T recipe) {
        this.writeToServer(buf, recipe);
    }



}
