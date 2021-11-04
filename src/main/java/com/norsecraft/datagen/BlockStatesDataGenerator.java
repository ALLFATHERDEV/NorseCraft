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

/**
 * This class generates all block states
 * <p>
 * by OdinAllfather
 */
public class BlockStatesDataGenerator implements NorseCraftDataGenerator {

    private final int[] empty = {0, 0, 0, 0};

    @Override
    public void generate() {

        //Simple Blocks
        generateSimpleBlock(() -> NCBlocks.COPPER_ORE);
        generateSimpleBlock(() -> NCBlocks.BRONZE_ORE);
        generateSimpleBlock(() -> NCBlocks.IRITHIUM_ORE);
        generateSimpleBlock(() -> NCBlocks.SILVER_ORE);
        generateSimpleBlock(() -> NCBlocks.DARK_COBBLESTONE);
        generateSimpleBlock(() -> NCBlocks.DARK_COBBLESTONE_MOSSY);
        generateSimpleBlock(() -> NCBlocks.DARK_STONE);
        generateSimpleBlock(() -> NCBlocks.DARK_STONE_BRICK);
        generateSimpleBlock(() -> NCBlocks.DARK_STONE_BRICK_MOSSY);
        generateSimpleBlock(() -> NCBlocks.COBBLE_WALL);
        generateSimpleBlock(() -> NCBlocks.DWARF_FORGE_PIT, "dwarf_forge");

        //Variants
        generateVariantBlock(() -> NCBlocks.COLOURED_STONE, "coloured_stone", 4);
        generateVariantBlock(() -> NCBlocks.BRICKS_BLUEISH, "bricks/blue", 5);
        generateVariantBlock(() -> NCBlocks.BRICKS_GRAY, "bricks/gray", 6);
        generateVariantBlock(() -> NCBlocks.STICKS, "sticks", 4);

        generateStonePillarBlock(() -> NCBlocks.STONE_PILLAR);

        //Direction and Side
        generateWithSideAndDirectionalProperty(() -> NCBlocks.DWARF_FORGE_WALL, false, "dwarf_forge", new int[]{0, 90, 180, -90}, new int[]{0, 90, 180, -90});
        generateWithSideAndDirectionalProperty(() -> NCBlocks.DWARF_FORGE_CHIMNEY_WALL, false, "dwarf_forge", new int[]{180, -90, 0, 90}, new int[]{180, -90, 0, 90});
        generateWithSideAndDirectionalProperty(() -> NCBlocks.DWARF_FORGE_PILLAR, false, "dwarf_forge", new int[]{0, 90, 180, -90}, new int[]{0, 90, 180, -90});

        //Chairs
        int[] chairsRotation = new int[]{180, 90, 90, 0};
        generateDirectional(() -> NCBlocks.ACACIA_WOODEN_CHAIR_1, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.ACACIA_WOODEN_CHAIR_2, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.ACACIA_WOODEN_CHAIR_3, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.ACACIA_WOODEN_CHAIR_4, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.ACACIA_WOODEN_CHAIR_5, "chairs", chairsRotation);

        generateDirectional(() -> NCBlocks.BIRCH_WOODEN_CHAIR_1, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.BIRCH_WOODEN_CHAIR_2, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.BIRCH_WOODEN_CHAIR_3, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.BIRCH_WOODEN_CHAIR_4, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.BIRCH_WOODEN_CHAIR_5, "chairs", chairsRotation);

        generateDirectional(() -> NCBlocks.DARK_OAK_WOODEN_CHAIR_1, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.DARK_OAK_WOODEN_CHAIR_2, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.DARK_OAK_WOODEN_CHAIR_3, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.DARK_OAK_WOODEN_CHAIR_4, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.DARK_OAK_WOODEN_CHAIR_5, "chairs", chairsRotation);

        generateDirectional(() -> NCBlocks.JUNGLE_WOODEN_CHAIR_1, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.JUNGLE_WOODEN_CHAIR_2, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.JUNGLE_WOODEN_CHAIR_3, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.JUNGLE_WOODEN_CHAIR_4, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.JUNGLE_WOODEN_CHAIR_5, "chairs", chairsRotation);

        generateDirectional(() -> NCBlocks.OAK_WOODEN_CHAIR_1, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.OAK_WOODEN_CHAIR_2, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.OAK_WOODEN_CHAIR_3, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.OAK_WOODEN_CHAIR_4, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.OAK_WOODEN_CHAIR_5, "chairs", chairsRotation);

        generateDirectional(() -> NCBlocks.SPRUCE_WOODEN_CHAIR_1, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.SPRUCE_WOODEN_CHAIR_2, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.SPRUCE_WOODEN_CHAIR_3, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.SPRUCE_WOODEN_CHAIR_4, "chairs", chairsRotation);
        generateDirectional(() -> NCBlocks.SPRUCE_WOODEN_CHAIR_5, "chairs", chairsRotation);

        generateWithSideAndDirectionalProperty(() -> NCBlocks.ACACIA_BENCH_1, true, "bench/1", new int[]{90, 180, -90, 0}, new int[]{90, 180, -90, 0});
        generateWithSideAndDirectionalProperty(() -> NCBlocks.BIRCH_BENCH_1, true, "bench/1", new int[]{90, 180, -90, 0}, new int[]{90, 180, -90, 0});
        generateWithSideAndDirectionalProperty(() -> NCBlocks.JUNGLE_BENCH_1, true, "bench/1", new int[]{90, 180, -90, 0}, new int[]{90, 180, -90, 0});
        generateWithSideAndDirectionalProperty(() -> NCBlocks.DARK_OAK_BENCH_1, true, "bench/1", new int[]{90, 180, -90, 0}, new int[]{90, 180, -90, 0});
        generateWithSideAndDirectionalProperty(() -> NCBlocks.OAK_BENCH_1, true, "bench/1", new int[]{90, 180, -90, 0}, new int[]{90, 180, -90, 0});
        generateWithSideAndDirectionalProperty(() -> NCBlocks.SPRUCE_BENCH_1, true, "bench/1", new int[]{90, 180, -90, 0}, new int[]{90, 180, -90, 0});

    }

    private void generateSimpleBlock(Supplier<Block> block) {
        this.generateSimpleBlock(block, null);
    }

    /**
     * Generate a simple block states file. the file will look like this:
     * <p>
     * {
     * "variants": {
     * "": {
     * "model": "norsecraft:block/additionalPath/blockName"
     * }
     * }
     * }
     *
     * @param block          the block object
     * @param additionalPath the additional path to the model
     */
    private void generateSimpleBlock(Supplier<Block> block, String additionalPath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject main = new JsonObject();
        JsonObject variants = new JsonObject();
        JsonObject all = new JsonObject();
        String blockName = Registry.BLOCK.getId(block.get()).getPath();
        all.addProperty("model", "norsecraft:block/" + (additionalPath != null ? additionalPath + "/" + blockName : blockName));
        variants.add("", all);
        main.add("variants", variants);
        String jsonString = gson.toJson(main);
        this.save(jsonString, blockName);
    }

    /**
     * Generates a block states file with directional properties states
     *
     * @param block              the block object
     * @param additionalPath     the additional path to the model
     * @param yRotationModifiers add "y" rotation to the states.
     *                           Index 0: North
     *                           Index 1: East
     *                           Index 2: South
     *                           Index 3: West
     */
    private void generateDirectional(Supplier<Block> block, String additionalPath, int[] yRotationModifiers) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonObject main = new JsonObject();
        JsonObject variants = new JsonObject();
        String blockName = Registry.BLOCK.getId(block.get()).getPath();

        String modelPath = getModelPath(additionalPath, blockName);
        variants.add("facing=north", buildModelObject(modelPath, yRotationModifiers[0]));
        variants.add("facing=east", buildModelObject(modelPath, yRotationModifiers[1]));
        variants.add("facing=south", buildModelObject(modelPath, yRotationModifiers[2]));
        variants.add("facing=west", buildModelObject(modelPath, yRotationModifiers[3]));
        main.add("variants", variants);

        this.save(gson.toJson(main), blockName);
    }

    private void generateWithSideAndDirectionalProperty(Supplier<Block> block, boolean leftRight, String additionalPath, int[] yRotationMidifierSide1, int[] yRotationModifierSide2) {
        this.generateWithSideAndDirectionalProperty(block, leftRight, additionalPath, yRotationMidifierSide1, yRotationModifierSide2, false);
    }

    /**
     * This method is for double sized blocks or tripple sized blocks with y rotation modifiers. Like the bench
     *
     * @param block                   the block object
     * @param leftRight               if true it will use the horizontal names if false the vertical names
     *                                horizontal names: _left and _right
     *                                vertical names: _top and _bottom
     * @param additionalPath          the additional path to the model path
     * @param yRotationModifiersSide1 the y rotation modifiers for the first side (left or top)
     * @param yRotationModifiersSide2 the y rotation modifiers for the second side (right or bottom)
     * @param blockHalf               if you use the property {@link net.minecraft.block.enums.BlockHalf} set this to true, if not set it to false
     */
    private void generateWithSideAndDirectionalProperty(Supplier<Block> block, boolean leftRight, String additionalPath, int[] yRotationModifiersSide1, int[] yRotationModifiersSide2, boolean blockHalf) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonObject main = new JsonObject();
        JsonObject variants = new JsonObject();
        String blockName = Registry.BLOCK.getId(block.get()).getPath();
        String side1 = leftRight ? "left" : "top";
        String side2 = leftRight ? "right" : "bottom";
        String modelPathSide1 = getModelPath(additionalPath, blockName + (leftRight ? "_left" : "_top"));
        String modelPathSide2 = getModelPath(additionalPath, blockName + (leftRight ? "_right" : "_bottom"));

        //Side1
        String sidePropName = blockHalf ? "half" : "side";
        variants.add(String.format("%s=%s,facing=north", sidePropName, side1), buildModelObject(modelPathSide1, yRotationModifiersSide1[0]));
        variants.add(String.format("%s=%s,facing=east", sidePropName, side1), buildModelObject(modelPathSide1, yRotationModifiersSide1[1]));
        variants.add(String.format("%s=%s,facing=south", sidePropName, side1), buildModelObject(modelPathSide1, yRotationModifiersSide1[2]));
        variants.add(String.format("%s=%s,facing=west", sidePropName, side1), buildModelObject(modelPathSide1, yRotationModifiersSide1[3]));
        //Side2
        variants.add(String.format("%s=%s,facing=north", sidePropName, side2), buildModelObject(modelPathSide2, yRotationModifiersSide2[0]));
        variants.add(String.format("%s=%s,facing=east", sidePropName, side2), buildModelObject(modelPathSide2, yRotationModifiersSide2[1]));
        variants.add(String.format("%s=%s,facing=south", sidePropName, side2), buildModelObject(modelPathSide2, yRotationModifiersSide2[2]));
        variants.add(String.format("%s=%s,facing=west", sidePropName, side2), buildModelObject(modelPathSide2, yRotationModifiersSide2[3]));
        main.add("variants", variants);

        this.save(gson.toJson(main), blockName);
    }

    /**
     * If you have a block with more variants (like the coloured stones or bricks) use this method
     *
     * @param block          the block object
     * @param additionalPath the additional path to the model
     * @param variantsCount  the amount of the different variants
     */
    private void generateVariantBlock(Supplier<Block> block, String additionalPath, int variantsCount) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonObject main = new JsonObject();
        JsonObject variants = new JsonObject();
        String blockName = Registry.BLOCK.getId(block.get()).getPath();

        for (int i = 0; i < variantsCount; i++) {
            int j = i + 1;
            String modelPath = getModelPath(additionalPath, blockName + "_" + i);
            variants.add(String.format("block_variants=%d", j), buildModelObject(modelPath, 0));
        }
        main.add("variants", variants);

        this.save(gson.toJson(main), blockName);
    }

    /**
     * This is explicit for the pillar block bcs it is strange :-)
     *
     * @param block the block object
     */
    private void generateStonePillarBlock(Supplier<Block> block) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonObject main = new JsonObject();
        JsonObject variants = new JsonObject();
        String blockName = Registry.BLOCK.getId(block.get()).getPath();
        variants.addProperty("pillar_height=bottom", getModelPath(null, blockName + "_bottom"));
        variants.addProperty("pillar_height=middle", getModelPath(null, blockName + "_mid"));
        variants.addProperty("pillar_height=top", getModelPath(null, blockName + "_top"));
        main.add("variants", variants);

        this.save(gson.toJson(main), blockName);
    }

    /**
     * @param additionalPath the additional path to the model
     * @param blockName      the block name
     * @return the exact model path to the model file
     */
    private String getModelPath(String additionalPath, String blockName) {
        return "norsecraft:block/" + (additionalPath != null ? additionalPath + "/" + blockName : blockName);
    }


    /**
     * This method builds the json object for every property
     *
     * @param modelPath the model path
     * @param y         the y rotation modifier. If 0 then it will be ignored
     * @return the builded json object
     */
    private JsonObject buildModelObject(String modelPath, int y) {
        JsonObject object = new JsonObject();
        object.addProperty("model", modelPath);
        if (y != 0)
            object.addProperty("y", y);
        return object;
    }


    /**
     * Save the raw json string into the file
     *
     * @param jsonString the json string
     * @param blockName  the file name as blockName
     */
    private void save(String jsonString, String blockName) {
        try {
            File folder = new File("../data_generator/blockstates/");
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
        return "Block States";
    }
}
