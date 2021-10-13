package com.norsecraft.common.recipe;

import com.google.gson.JsonObject;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.util.NCID;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a base class for all our custom recipe serializer, bcs this class has NBT support for items
 *
 * @param <T> the recipe type
 */
public abstract class NCRecipeSerializer<T extends Recipe<? extends Inventory>> implements RecipeSerializer<T> {

    protected Logger logger = LogManager.getLogger("NC Recipe Serializer");

    /**
     * This method reads the recipe from the json file
     *
     * @param id   the id from the recipe
     * @param json the json object
     * @return the recipe
     */
    public abstract T readFromJson(NCID id, JsonObject json);

    /**
     * This method reads the recipe from the server
     *
     * @param id  the id from the recipe
     * @param buf the byte buf with the recipe data
     * @return the recipe
     */
    public abstract T readFromServer(NCID id, PacketByteBuf buf);

    /**
     * This method is to write all the recipe data to the server
     *
     * @param buf    the byte buf for the data
     * @param recipe the recipe to write
     */
    public abstract void writeToServer(PacketByteBuf buf, T recipe);

    protected JsonObject nbtToJson(ItemStack item) {
        JsonObject main = new JsonObject();
        NbtCompound nbt = item.getNbt();
        if(nbt == null) {
            logger.warn("NBT data for the item {} is null. Could not create a json object. Return empty object", Registry.ITEM.getId(item.getItem()).toString());
            return main;
        }
        return null;
    }

    @Override
    public T read(Identifier id, JsonObject json) {
        return this.readFromJson(NCID.of(id), json);
    }

    @Override
    public T read(Identifier id, PacketByteBuf buf) {
        return this.readFromServer(NCID.of(id), buf);
    }

    @Override
    public void write(PacketByteBuf buf, T recipe) {
        this.writeToServer(buf, recipe);
    }
}
