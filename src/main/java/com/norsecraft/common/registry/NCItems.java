package com.norsecraft.common.registry;

import com.norsecraft.NorseCraftMod;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NCItems {

    public static void register() {
        NorseCraftMod.LOGGER.info("Register items");
    }

    public static <T extends Item> void registerItem(String name, T item) {
        Registry.register(Registry.ITEM, new Identifier(NorseCraftMod.MOD_ID, name), item);
    }

}
