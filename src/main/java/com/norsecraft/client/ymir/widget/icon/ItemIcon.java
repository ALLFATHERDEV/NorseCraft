package com.norsecraft.client.ymir.widget.icon;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Objects;

/**
 * This class represent an item icon
 */
public class ItemIcon implements Icon{

    private final ItemStack stack;

    public ItemIcon(ItemStack stack) {
        this.stack = Objects.requireNonNull(stack, "stack");
    }

    public ItemIcon(Item item) {
        this(Objects.requireNonNull(item, "item").getDefaultStack());
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int size) {
        MinecraftClient client = MinecraftClient.getInstance();
        ItemRenderer renderer = client.getItemRenderer();
        MatrixStack modelViewMatrices = RenderSystem.getModelViewStack();

        float scale = size != 16 ? ((float) size / 16f) : 1f;
        modelViewMatrices.push();
        modelViewMatrices.translate(x, y, 0);
        modelViewMatrices.scale(scale, scale, 1);
        RenderSystem.applyModelViewMatrix();
        renderer.renderInGui(stack, 0, 0);
        modelViewMatrices.pop();
        RenderSystem.applyModelViewMatrix();
    }
}
