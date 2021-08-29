package com.norsecraft.common.registry;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.block.*;
import com.norsecraft.common.block.dwarfforge.DwarfForgeChimneyBlock;
import com.norsecraft.common.block.dwarfforge.DwarfForgePillarBlock;
import com.norsecraft.common.block.dwarfforge.DwarfForgePitBlock;
import com.norsecraft.common.block.dwarfforge.DwarfForgeWallBlock;
import com.norsecraft.common.util.VoxelShapeGroups;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NCBlocks {

    private static final FabricBlockSettings ORE_PROPS = FabricBlockSettings.of(Material.METAL);

    public static final Block COPPER_ORE = new OreBlock(ORE_PROPS);
    public static final Block BRONZE_ORE = new OreBlock(ORE_PROPS);
    public static final Block IRITHIUM_ORE = new OreBlock(ORE_PROPS);

    public static final Block LOG_CHAIR = new BaseDirectionalBlock(FabricBlockSettings.of(Material.WOOD), VoxelShapeGroups.LOG_CHAIR_GROUP);
    public static final Block WOODEN_CHAIR_1 = new BaseDirectionalBlock(FabricBlockSettings.of(Material.WOOD), VoxelShapeGroups.WOODEN_CHAIR_1);
    public static final Block WOODEN_CHAIR_2 = new BaseDirectionalBlock(FabricBlockSettings.of(Material.WOOD), VoxelShapeGroups.WOODEN_CHAIR_2);
    public static final Block WOODEN_CHAIR_3 = new BaseDirectionalBlock(FabricBlockSettings.of(Material.WOOD), VoxelShapeGroups.WOODEN_CHAIR_3);
    public static final Block WOODEN_DOUBLE_TABLE = new DoubleWoodenTableBlock(FabricBlockSettings.of(Material.WOOD));

    public static final Block OLD_CAMPFIRE = new BaseDirectionalBlock(FabricBlockSettings.of(Material.WOOD), VoxelShapeGroups.OLD_CAMPFIRE);
    public static final Block WOOD_TABLE = new BaseDirectionalBlock(FabricBlockSettings.of(Material.WOOD), VoxelShapeGroups.WOOD_TABLE);

    public static final Block DARK_COBBLESTONE = new Block(settingsHard(40.0F, 1000.0F));
    public static final Block DARK_COBBLESTONE_MOSSY = new Block(settingsHard(40.0F, 1000.0F));
    public static final Block DARK_STONE = new Block(settingsHard(70.0F, 1200.0F));
    public static final Block DARK_STONE_BRICK = new Block(settingsHard(40.0F, 1000.0F));
    public static final Block DARK_STONE_BRICK_MOSSY = new Block(settingsHard(40.0F, 1000.0F));
    public static final Block COLOURED_STONE = new BaseVariantsBlockVar4(settingsHard(2.0F, 5.0F));
    public static final Block BRICKS_BLUEISH = new Block(FabricBlockSettings.copyOf(Blocks.BRICKS));
    public static final Block BRICKS_GRAY = new Block(FabricBlockSettings.copyOf(Blocks.BRICKS));

    public static final Block STONE_PILLAR = new StonePillarBlock(FabricBlockSettings.of(Material.STONE));
    public static final Block CRATE = new CrateBlock(FabricBlockSettings.copyOf(Blocks.CHEST));

    //DWARF FORGE
    public static final Block DWARF_FORGE_PIT = new DwarfForgePitBlock();
    public static final Block DWARF_FORGE_WALL = new DwarfForgeWallBlock();
    public static final Block DWARF_FORGE_CHIMNEY_WALL = new DwarfForgeChimneyBlock();
    public static final Block DWARF_FORGE_PILLAR = new DwarfForgePillarBlock();

    public static void register() {
        registerBlock("copper_ore", COPPER_ORE);
        registerBlock("bronze_ore", BRONZE_ORE);
        registerBlock("irithium_ore", IRITHIUM_ORE);

        registerBlock("log_chair", LOG_CHAIR);
        registerBlock("wooden_chair_1", WOODEN_CHAIR_1);
        registerBlock("wooden_chair_2", WOODEN_CHAIR_2);
        registerBlock("wooden_chair_3", WOODEN_CHAIR_3);
        registerBlock("wooden_double_table", WOODEN_DOUBLE_TABLE);

        registerBlock("old_campfire", OLD_CAMPFIRE);
        registerBlock("wooden_table", WOOD_TABLE);

        registerBlock("dark_cobblestone", DARK_COBBLESTONE);
        registerBlock("dark_cobblestone_mossy", DARK_COBBLESTONE_MOSSY);
        registerBlock("dark_stone", DARK_STONE);
        registerBlock("dark_stone_brick", DARK_STONE_BRICK);
        registerBlock("dark_stone_brick_mossy", DARK_STONE_BRICK_MOSSY);
        registerBlock("coloured_stone", COLOURED_STONE);
        registerBlock("bricks_blueish", BRICKS_BLUEISH);
        registerBlock("bricks_gray", BRICKS_GRAY);

        registerBlock("stone_pillar", STONE_PILLAR);
        registerBlock("crate", CRATE);

        registerBlock("dwarf_forge_pit", DWARF_FORGE_PIT);
        registerBlock("dwarf_forge_wall", DWARF_FORGE_WALL);
        registerBlock("dwarf_forge_chimney_wall", DWARF_FORGE_CHIMNEY_WALL);
        registerBlock("dwarf_forge_pillar", DWARF_FORGE_PILLAR);

    }

    public static <T extends Block> void registerBlock(String name, T block) {
        Registry.register(Registry.BLOCK, new Identifier(NorseCraftMod.MOD_ID, name), block);
        registerBlockItem(name, block);
    }

    private static <T extends Block> void registerBlockItem(String name, T block) {
        Registry.register(Registry.ITEM, new Identifier(NorseCraftMod.MOD_ID, name), new BlockItem(block, new FabricItemSettings().group(NorseCraftMod.GROUP)));
    }

    private static AbstractBlock.Settings settingsHard(float hardness, float resistance) {
        return FabricBlockSettings.of(Material.METAL).requiresTool().hardness(hardness).resistance(resistance);
    }

}
