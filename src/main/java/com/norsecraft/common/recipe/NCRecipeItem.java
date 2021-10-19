package com.norsecraft.common.recipe;

import com.google.gson.JsonObject;
import com.norsecraft.common.util.nbt.NbtDeserializer;
import com.norsecraft.common.util.nbt.NbtSerializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

/**
 * This class is a small helper class for the recipe builder
 * if you create a recipe builder, and you want to use items with nbt support use this class
 */
public class NCRecipeItem {

    private final NbtSerializer serializer = new NbtSerializer();
    private final NbtDeserializer deserializer = new NbtDeserializer();

    @NotNull
    private ItemStack itemStack;

    private NCRecipeItem(@NotNull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public void withNbt(NbtCompound nbt) {
        itemStack.setNbt(nbt);
    }

    @NotNull
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public String getItemName() {
        return Registry.ITEM.getId(this.itemStack.getItem()).toString();
    }

    /**
     * Saves the item into the json object
     *
     * @param key the nbt data key
     * @param object the object where it should be saved
     */
    public void write(String key, JsonObject object) {
        object.addProperty("item", this.getItemName());
        if(this.itemStack.getNbt() != null) {
            NbtCompound nbt = this.itemStack.getNbt();
            object.add(key, this.serializer.serialize(nbt));
        }
    }

    /**
     * Reads the item from the json object
     *
     * @param object the json object that holds the item
     */
    public void read(JsonObject object) {
        String itemName = object.get("item").getAsString();
        String prefix = itemName.split(":")[0];
        if(prefix.equals("minecraft"))
            this.itemStack = Registry.ITEM.get(new Identifier("minecraft", itemName.split(":")[1])).getDefaultStack();
        else if(prefix.equals("norsecraft"))
            this.itemStack = Registry.ITEM.get(new Identifier("norsecraft", itemName.split(":")[1])).getDefaultStack();
        else
            throw new IllegalArgumentException("Could not find prefix " + prefix);

        NbtCompound nbt = this.deserializer.deserialize(object);
        this.itemStack.setNbt(nbt);
    }

    /**
     * Create a recipe item object
     *
     * @param item the item
     * @param nbt the nbt compound
     * @return the created recipe item object
     */
    public static NCRecipeItem of(Item item, NbtCompound nbt) {
        NCRecipeItem ncRecipeItem = new NCRecipeItem(item.getDefaultStack());
        ncRecipeItem.withNbt(nbt);
        return ncRecipeItem;
    }

    /**
     * Create a recipe item object without nbt
     *
     * @param item the item
     * @return the created recipe item object
     */
    public static NCRecipeItem of(Item item) {
        return new NCRecipeItem(item.getDefaultStack());
    }

}
