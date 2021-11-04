package com.norsecraft.common.registry;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.item.CampfireStandItem;
import com.norsecraft.common.item.CoinPurseItem;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Register all the items from the mod
 */
public class NCItems {

    public static final Item ROCK = new Item(defSettings());
    public static final Item CAMPFIRE_STAND = new CampfireStandItem(defSettings());
    public static final Item COPPER_COIN = new Item(defSettings());
    public static final Item SILVER_COIN = new Item(defSettings());
    public static final Item GOLD_COIN = new Item(defSettings());
    public static final Item COIN_PURSE = new CoinPurseItem(defSettings().maxCount(1));

    public static void register() {
        NorseCraftMod.LOGGER.info("Register items");

        registerItem("rock", ROCK);
        registerItem("campfire_stand", CAMPFIRE_STAND);
        registerItem("copper_coin", COPPER_COIN);
        registerItem("silver_coin", SILVER_COIN);
        registerItem("gold_coin", GOLD_COIN);
        registerItem("coin_purse", COIN_PURSE);
    }

    public static <T extends Item> void registerItem(String name, T item) {
        Registry.register(Registry.ITEM, new Identifier(NorseCraftMod.MOD_ID, name), item);
    }

    private static Item.Settings defSettings() {
        return new Item.Settings().group(NorseCraftMod.GROUP);
    }

}
