package com.norsecraft.common.gui;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.ymir.interpretation.CookingGuiInterpretation;
import com.norsecraft.client.ymir.screen.BackgroundPainter;
import com.norsecraft.client.ymir.screen.TexturedBackgroundPainter;
import com.norsecraft.client.ymir.widget.YmirItemSlot;
import com.norsecraft.client.ymir.widget.YmirPlainPanel;
import com.norsecraft.client.ymir.widget.YmirProgressBar;
import com.norsecraft.client.ymir.widget.data.Texture;
import com.norsecraft.common.registry.NCScreenHandlers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerContext;

public class CampfireGuiInterpretation extends CookingGuiInterpretation {

    private static final BackgroundPainter PAINTER = new TexturedBackgroundPainter(new Texture(NorseCraftMod.ncTex("gui/campfire_basic.png"),
            0, 0, 352, 309, 512, 512));

    public CampfireGuiInterpretation(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY, new ArrayPropertyDelegate(4));
    }

    public CampfireGuiInterpretation(int syncId, PlayerInventory playerInventory, ScreenHandlerContext ctx, PropertyDelegate propertyDelegate) {
        super(NCScreenHandlers.campfireGuiBasic, RecipeBookCategory.FURNACE, syncId, playerInventory, ctx, propertyDelegate, null, 4);

        YmirPlainPanel panel = new YmirPlainPanel();
        panel.setBackgroundPainter(PAINTER);

        Texture barTexture = Texture.component(130, 0, 144, 14);
        YmirProgressBar progressBar = new YmirProgressBar(barTexture, 0, 1, YmirProgressBar.Direction.UP);
        panel.add(progressBar, 63, 42, 13, 13);
        panel.add(YmirItemSlot.of(blockInventory, 0), 61, 43);
        panel.add(YmirItemSlot.of(blockInventory, 1), 61, 7);
        panel.add(YmirItemSlot.of(blockInventory, 2), 121, 25);
        YmirItemSlot bowl = YmirItemSlot.of(blockInventory, 3);
        bowl.setFilter(itemStack -> itemStack.getItem().equals(Items.BOWL));
        panel.add(bowl, 40, 7);

        panel.add(this.createPlayerInventoryPanel(), 7, 61);


        this.setRootPanel(panel);
        panel.validate(this);

    }

    @Override
    public void clearCraftingSlots() {
        this.getSlot(0).setStack(ItemStack.EMPTY);
        this.getSlot(2).setStack(ItemStack.EMPTY);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return 2;
    }

    @Override
    public int getCraftingWidth() {
        return 1;
    }

    @Override
    public int getCraftingHeight() {
        return 1;
    }

    @Override
    public int getCraftingSlotCount() {
        return 3;
    }
}
