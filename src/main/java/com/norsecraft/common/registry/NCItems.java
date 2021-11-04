package com.norsecraft.common.registry;

import com.norsecraft.NorseCraftMod;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Register all the items from the mod
 */
public class NCItems {

    public static final Item ROCK = new Item(defSettings());
    public static final Item CAMPFIRE_STAND = new Item(defSettings());

    public static void register() {
        NorseCraftMod.LOGGER.info("Register items");

        registerItem("rock", ROCK);
        registerItem("campfire_stand", CAMPFIRE_STAND);
    }

    public static <T extends Item> void registerItem(String name, T item) {
        Registry.register(Registry.ITEM, new Identifier(NorseCraftMod.MOD_ID, name), item);
    }

    private static Item.Settings defSettings() {
        return new Item.Settings().group(NorseCraftMod.GROUP);
    }

}
