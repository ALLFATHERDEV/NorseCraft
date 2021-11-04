package com.norsecraft.common.recipe;

import com.google.gson.JsonObject;
import com.norsecraft.common.util.nbt.NbtDeserializer;
import com.norsecraft.common.util.nbt.NbtHelper;
import com.norsecraft.common.util.nbt.NbtSerializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataOutputStream;

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

    private NCRecipeItem(JsonObject object) {
        this.itemStack = ItemStack.EMPTY;
        this.read(object);
    }

    private NCRecipeItem(PacketByteBuf buf) {
        this.itemStack = buf.readItemStack();
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
     * Tests if the items are equals with nbt support
     *
     * @param item the item to check
     * @return true if the items are equals otherwise false
     */
    public boolean test(@Nullable NCRecipeItem item) {
        if(item == null)
            return false;
        boolean flag = this.itemStack.isItemEqual(item.itemStack) || this.itemStack.equals(item.itemStack);
        NbtCompound firstNbt = this.itemStack.getNbt();
        NbtCompound secondNbt = item.itemStack.getNbt();
        boolean nbt = true;
        if(firstNbt != null && secondNbt != null)
            nbt = NbtHelper.equals(firstNbt, secondNbt);

        return flag && nbt;
    }

    public Ingredient toIngredient() {
        return Ingredient.ofStacks(this.itemStack);
    }

    /**
     * Saves the item into the json object
     *
     * @param object the object where it should be saved
     */
    public void write(JsonObject object) {
        object.addProperty("item", this.getItemName());
        if(this.itemStack.getNbt() != null) {
            NbtCompound nbt = this.itemStack.getNbt();
            object.add("data", this.serializer.serialize(nbt));
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

        NbtCompound nbt = this.deserializer.deserialize(object.getAsJsonObject("data"));
        this.itemStack.setNbt(nbt);
    }

    /**
     * Writes the item to a byte buf
     *
     * @param buf the byte buf
     */
    public void netWrite(PacketByteBuf buf) {
        buf.writeItemStack(this.itemStack);
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

    /**
     * Creates a recipe item object from a json object, it uses the {@link NCRecipeItem#read(JsonObject)} method
     *
     * @param object the object where it is stored
     * @return the recipe object
     */
    public static NCRecipeItem of(JsonObject object) {
        return new NCRecipeItem(object);
    }

    /**
     * Creates a recipe item object with an item stack
     *
     * @param itemStack the itemstack
     * @return the created recipe item object
     */
    public static NCRecipeItem of(ItemStack itemStack) {
        return new NCRecipeItem(itemStack);
    }

    /**
     * Creates a recipe item object from a byte buf it uses the {@link NCRecipeItem#netRead(PacketByteBuf) method}
     *
     * @param buf the byte buf
     * @return the created recipe item object
     */
    public static NCRecipeItem of(PacketByteBuf buf) {
        return new NCRecipeItem(buf);
    }

}
