package com.norsecraft.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.norsecraft.common.registry.NCBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Supplier;

public class ItemModelDataGenerator implements NorseCraftDataGenerator {

    /**
     * This method generates all the files
     */
    @Override
    public void generate() {
        generateBlockItem(() -> NCBlocks.COPPER_ORE, null);
        generateBlockItem(() -> NCBlocks.BRONZE_ORE, null);
        generateBlockItem(() -> NCBlocks.IRITHIUM_ORE, null);
        generateBlockItem(() -> NCBlocks.SILVER_ORE, null);

        generateBlockItem(() -> NCBlocks.WOODEN_DOUBLE_TABLE, "","_left");
        generateBlockItem(() -> NCBlocks.CAMPFIRE, null);
        generateBlockItem(() -> NCBlocks.WOOD_TABLE, null);

        generateBlockItem(() -> NCBlocks.DARK_COBBLESTONE, null);
        generateBlockItem(() -> NCBlocks.DARK_COBBLESTONE_MOSSY, null);
        generateBlockItem(() -> NCBlocks.DARK_STONE, null);
        generateBlockItem(() -> NCBlocks.DARK_STONE_BRICK, null);
        generateBlockItem(() -> NCBlocks.DARK_STONE_BRICK_MOSSY, null);
        generateBlockItem(() -> NCBlocks.COLOURED_STONE, "", "_0");
        generateBlockItem(() -> NCBlocks.ROCKS, "","_0");
        generateBlockItem(() -> NCBlocks.COBBLE_WALL, null);

        generateBlockItem(() -> NCBlocks.STONE_PILLAR, null);
        generateBlockItem(() -> NCBlocks.CRATE, null);
        generateBlockItem(() -> NCBlocks.STICKS, "sticks", "_0");

        generateBlockItem(() -> NCBlocks.DWARF_FORGE_PIT, "dwarf_forge");
        generateBlockItem(() -> NCBlocks.DWARF_FORGE_WALL, "dwarf_forge", "_bottom");
        generateBlockItem(() -> NCBlocks.DWARF_FORGE_CHIMNEY_WALL, "dwarf_forge", "_bottom");
        generateBlockItem(() -> NCBlocks.DWARF_FORGE_PILLAR, "dwarf_forge", "_bottom");

        generateBlockItem(() -> NCBlocks.ACACIA_WOODEN_CHAIR_1, "chairs");
        generateBlockItem(() -> NCBlocks.ACACIA_WOODEN_CHAIR_2, "chairs");
        generateBlockItem(() -> NCBlocks.ACACIA_WOODEN_CHAIR_3, "chairs");
        generateBlockItem(() -> NCBlocks.ACACIA_WOODEN_CHAIR_4, "chairs");
        generateBlockItem(() -> NCBlocks.ACACIA_WOODEN_CHAIR_5, "chairs");

        generateBlockItem(() -> NCBlocks.BIRCH_WOODEN_CHAIR_1, "chairs");
        generateBlockItem(() -> NCBlocks.BIRCH_WOODEN_CHAIR_2, "chairs");
        generateBlockItem(() -> NCBlocks.BIRCH_WOODEN_CHAIR_3, "chairs");
        generateBlockItem(() -> NCBlocks.BIRCH_WOODEN_CHAIR_4, "chairs");
        generateBlockItem(() -> NCBlocks.BIRCH_WOODEN_CHAIR_5, "chairs");

        generateBlockItem(() -> NCBlocks.DARK_OAK_WOODEN_CHAIR_1, "chairs");
        generateBlockItem(() -> NCBlocks.DARK_OAK_WOODEN_CHAIR_2, "chairs");
        generateBlockItem(() -> NCBlocks.DARK_OAK_WOODEN_CHAIR_3, "chairs");
        generateBlockItem(() -> NCBlocks.DARK_OAK_WOODEN_CHAIR_4, "chairs");
        generateBlockItem(() -> NCBlocks.DARK_OAK_WOODEN_CHAIR_5, "chairs");

        generateBlockItem(() -> NCBlocks.JUNGLE_WOODEN_CHAIR_1, "chairs");
        generateBlockItem(() -> NCBlocks.JUNGLE_WOODEN_CHAIR_2, "chairs");
        generateBlockItem(() -> NCBlocks.JUNGLE_WOODEN_CHAIR_3, "chairs");
        generateBlockItem(() -> NCBlocks.JUNGLE_WOODEN_CHAIR_4, "chairs");
        generateBlockItem(() -> NCBlocks.JUNGLE_WOODEN_CHAIR_5, "chairs");

        generateBlockItem(() -> NCBlocks.OAK_WOODEN_CHAIR_1, "chairs");
        generateBlockItem(() -> NCBlocks.OAK_WOODEN_CHAIR_2, "chairs");
        generateBlockItem(() -> NCBlocks.OAK_WOODEN_CHAIR_3, "chairs");
        generateBlockItem(() -> NCBlocks.OAK_WOODEN_CHAIR_4, "chairs");
        generateBlockItem(() -> NCBlocks.OAK_WOODEN_CHAIR_5, "chairs");

        generateBlockItem(() -> NCBlocks.SPRUCE_WOODEN_CHAIR_1, "chairs");
        generateBlockItem(() -> NCBlocks.SPRUCE_WOODEN_CHAIR_2, "chairs");
        generateBlockItem(() -> NCBlocks.SPRUCE_WOODEN_CHAIR_3, "chairs");
        generateBlockItem(() -> NCBlocks.SPRUCE_WOODEN_CHAIR_4, "chairs");
        generateBlockItem(() -> NCBlocks.SPRUCE_WOODEN_CHAIR_5, "chairs");

        generateBlockItem(() -> NCBlocks.ACACIA_BENCH_1, "bench/1", "_left");
        generateBlockItem(() -> NCBlocks.BIRCH_BENCH_1, "bench/1", "_left");
        generateBlockItem(() -> NCBlocks.DARK_OAK_BENCH_1, "bench/1", "_left");
        generateBlockItem(() -> NCBlocks.JUNGLE_BENCH_1, "bench/1", "_left");
        generateBlockItem(() -> NCBlocks.OAK_BENCH_1, "bench/1", "_left");
        generateBlockItem(() -> NCBlocks.SPRUCE_BENCH_1, "bench/1", "_left");

    }

    private void generateBlockItem(Supplier<Block> block, String additionalPath) {
       generateBlockItem(block, additionalPath, null);
    }

    /**
     * This method generate a item model file that looks like this:
     *
     * {
     *     "parent": "norsecraft:block/additionalPath/blockName
     * }
     *
     * That references to a block model as parent for the item
     *
     * @param block the block as parent
     * @param additionalPath the additionalPath for the block model
     *                       i.e: bricks: additionalPath = bricks/gray
     * @param additionalName if it is a double sized block you can add a additional name. i.e: _left or _right
     */
    private void generateBlockItem(Supplier<Block> block, String additionalPath, String additionalName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonObject main = new JsonObject();
        String blockName = Registry.BLOCK.getId(block.get()).getPath();
        main.addProperty("parent", "norsecraft:block/" + (additionalPath != null ? additionalPath + "/" + blockName + (additionalName != null ? additionalName : "")
                : blockName + (additionalName != null ? additionalName : "")));

        this.save(gson.toJson(main), blockName);
    }

    /**
     * Saves the json String into the file
     *
     * @param jsonString the raw json string
     * @param blockName the block name as file name
     */
    private void save(String jsonString, String blockName) {
        try {
            File folder = new File("../data_generator/models/item/");
            if (!folder.exists())
                if (!folder.mkdirs())
                    return;
            File file = new File(folder, blockName + ".json");
            FileWriter writer = new FileWriter(file);
            writer.write(jsonString);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDataGeneratorName() {
        return "Item Model";
    }
}
