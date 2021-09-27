package com.norsecraft.common.registry;

import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.Blocks;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

public class LootTableListener {

    private static final Identifier STONE_ID = Blocks.STONE.getLootTableId();

    public static void listen() {
        LootTableLoadingCallback.EVENT.register(((resourceManager, manager, id, table, setter) -> {
            if (STONE_ID.equals(id)) {
                FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                        .rolls(UniformLootNumberProvider.create(3, 6))
                        .withEntry(ItemEntry.builder(NCItems.ROCK).build());
                table.withPool(poolBuilder.build());
            }
        }));
    }

}
