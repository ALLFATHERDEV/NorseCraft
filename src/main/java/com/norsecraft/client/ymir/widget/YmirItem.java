package com.norsecraft.client.ymir.widget;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class YmirItem extends YmirWidget {

    private List<ItemStack> items;
    private int duration = 25;
    private int ticks = 0;
    private int current = 0;

    public YmirItem(List<ItemStack> items) {
        setItems(items);
    }

    public YmirItem(Tag<? extends ItemConvertible> tag) {
        this(getRenderStacks(tag));
    }

    public YmirItem(ItemStack stack) {
        this(Collections.singletonList(stack));
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void tick() {
        if(ticks++ >= duration) {
            ticks = 0;
            current = (current + 1) % items.size();
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        RenderSystem.enableDepthTest();
        MinecraftClient client = MinecraftClient.getInstance();
        ItemRenderer renderer = client.getItemRenderer();
        renderer.zOffset = 100f;
        renderer.renderInGui(items.get(current), x + getWidth() / 2 - 9, y + getHeight() / 2 - 9);
        renderer.zOffset = 0f;
    }

    public int getDuration() {
        return duration;
    }

    public YmirItem setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public YmirItem setItems(List<ItemStack> items) {
        Objects.requireNonNull(items, "stack == null");
        if (items.isEmpty()) throw new IllegalArgumentException("The stack list is empty!");

        this.items = items;

        current = 0;
        ticks = 0;

        return this;
    }

    private static List<ItemStack> getRenderStacks(Tag<? extends ItemConvertible> tag) {
        ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
        for(ItemConvertible item : tag.values())
            builder.add(new ItemStack(item));

        return builder.build();
    }

}
