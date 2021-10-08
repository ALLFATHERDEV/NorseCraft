package com.norsecraft.client.ymir.interpretation;

import com.norsecraft.client.ymir.screen.BackgroundPainter;
import com.norsecraft.client.ymir.widget.YmirPanel;
import com.norsecraft.client.ymir.widget.YmirPlainPanel;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.world.World;

public abstract class CookingGuiInterpretation extends SyncedGuiInterpretation implements RecipeGuiInterpretation<Inventory> {

    protected final World world;
    private final RecipeType<? extends AbstractCookingRecipe> recipeType;
    private final RecipeBookCategory category;

    public CookingGuiInterpretation(ScreenHandlerType<?> type, RecipeType<? extends AbstractCookingRecipe> recipeType,
                                    RecipeBookCategory category, int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx,
                                    PropertyDelegate propertyDelegate, BackgroundPainter painter) {
        super(type, syncId, playerInventory, getBlockInventory(ctx, 3), propertyDelegate, painter);
        this.recipeType = recipeType;
        this.category = category;
        checkDataCount(propertyDelegate, 4);
        this.world = playerInventory.player.world;
    }

    public void populateRecipeFinder(RecipeMatcher finder) {
        if(this.blockInventory instanceof RecipeInputProvider) {
            ((RecipeInputProvider) this.blockInventory).provideRecipeInputs(finder);
        }
    }

    public abstract void clearCraftingSlots();

    @Override
    public boolean matches(Recipe<? super Inventory> recipe) {
        return recipe.matches(this.blockInventory, this.world);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.blockInventory.canPlayerUse(player);
    }

    protected boolean isCookable(ItemStack itemStack) {
        return this.world.getRecipeManager().getFirstMatch(this.recipeType, new SimpleInventory(itemStack), this.world).isPresent();
    }

    protected boolean isFuel(ItemStack itemStack) {
        return AbstractFurnaceBlockEntity.canUseAsFuel(itemStack);
    }

    public int getCookProgress() {
        int i = this.propertyDelegate.get(2);
        int j = this.propertyDelegate.get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    public int getFuelProgress() {
        int i = this.propertyDelegate.get(1);
        if(i == 0)
            i = 200;
        return this.propertyDelegate.get(0) * 13 / i;
    }

    public boolean isBurning() {
        return this.propertyDelegate.get(0) > 0;
    }

    @Override
    public RecipeBookCategory getCategory() {
        return this.category;
    }

    @Override
    public boolean canInsertIntoSot(int slot) {
        return slot != 1;
    }
}
