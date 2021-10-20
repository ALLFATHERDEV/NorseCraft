package com.norsecraft.common.recipe.campfire;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.recipe.NCRecipeItem;
import com.norsecraft.common.registry.NCRecipeTypes;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class CampfireRecipe implements Recipe<Inventory> {

    public static final Identifier TYPE = NorseCraftMod.nc("campfire");

    private final Identifier id;
    private final NCRecipeItem input;
    private final NCRecipeItem output;
    private final boolean withBowl;

    public CampfireRecipe(Identifier id, NCRecipeItem input, NCRecipeItem output, boolean withBowl) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.withBowl = withBowl;
    }

    public NCRecipeItem getInput() {
        return input;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> ingredients = DefaultedList.of();
        ingredients.add(input.toIngredient());
        return ingredients;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return this.input.test(NCRecipeItem.of(inventory.getStack(0)));
    }

    @Override
    public ItemStack craft(Inventory inventory) {
        return this.output.getItemStack().copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput() {
        return this.output.getItemStack();
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    public boolean isWithBowl() {
        return withBowl;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.CAMPFIRE_COOKING;
    }

    @Override
    public RecipeType<?> getType() {
        return NCRecipeTypes.campfire;
    }
}
