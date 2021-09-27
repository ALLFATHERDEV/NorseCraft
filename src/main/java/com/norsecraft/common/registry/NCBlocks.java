package com.norsecraft.common.registry;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.block.*;
import com.norsecraft.common.block.dwarfforge.DwarfForgeChimneyBlock;
import com.norsecraft.common.block.dwarfforge.DwarfForgePillarBlock;
import com.norsecraft.common.block.dwarfforge.DwarfForgePitBlock;
import com.norsecraft.common.block.dwarfforge.DwarfForgeWallBlock;
import com.norsecraft.common.block.variants.BaseVariantsBlockVar3;
import com.norsecraft.common.block.variants.BaseVariantsBlockVar4;
import com.norsecraft.common.block.variants.BaseVariantsBlockVar5;
import com.norsecraft.common.block.variants.BaseVariantsBlockVar6;
import com.norsecraft.common.util.VoxelShapeGroup;
import com.norsecraft.common.util.VoxelShapeGroups;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NCBlocks {

    private static final FabricBlockSettings ORE_PROPS = FabricBlockSettings.copyOf(Blocks.DIAMOND_ORE);
    private static final FabricBlockSettings CHAIR_PROPS = FabricBlockSettings.copyOf(Blocks.OAK_WOOD);

    public static final Block COPPER_ORE = new OreBlock(ORE_PROPS);
    public static final Block BRONZE_ORE = new OreBlock(ORE_PROPS);
    public static final Block IRITHIUM_ORE = new OreBlock(ORE_PROPS);

    public static final Block LOG_CHAIR = new BaseDirectionalBlock(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS), VoxelShapeGroups.LOG_CHAIR_GROUP);
    public static final Block WOODEN_CHAIR_1 = new BaseDirectionalBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), VoxelShapeGroups.WOODEN_CHAIR_1);
    public static final Block WOODEN_CHAIR_2 = new BaseDirectionalBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), VoxelShapeGroups.WOODEN_CHAIR_2);
    public static final Block WOODEN_CHAIR_3 = new BaseDirectionalBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), VoxelShapeGroups.WOODEN_CHAIR_3);
    public static final Block WOODEN_DOUBLE_TABLE = new DoubleWoodenTableBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS));

    public static final Block OLD_CAMPFIRE = new BaseDirectionalBlock(FabricBlockSettings.of(Material.WOOD), VoxelShapeGroups.OLD_CAMPFIRE);
    public static final Block WOOD_TABLE = new BaseDirectionalBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), VoxelShapeGroups.WOOD_TABLE);

    public static final Block DARK_COBBLESTONE = new Block(FabricBlockSettings.copyOf(Blocks.COBBLESTONE));
    public static final Block DARK_COBBLESTONE_MOSSY = new Block(FabricBlockSettings.copyOf(Blocks.MOSSY_COBBLESTONE));
    public static final Block DARK_STONE = new Block(FabricBlockSettings.copyOf(Blocks.STONE));
    public static final Block DARK_STONE_BRICK = new Block(FabricBlockSettings.copyOf(Blocks.STONE_BRICKS));
    public static final Block DARK_STONE_BRICK_MOSSY = new Block(FabricBlockSettings.copyOf(Blocks.MOSSY_STONE_BRICKS));
    public static final Block COLOURED_STONE = new BaseVariantsBlockVar4(FabricBlockSettings.copyOf(Blocks.STONE));
    public static final Block BRICKS_BLUEISH = new BaseVariantsBlockVar5(FabricBlockSettings.copyOf(Blocks.BRICKS));
    public static final Block BRICKS_GRAY = new BaseVariantsBlockVar6(FabricBlockSettings.copyOf(Blocks.BRICKS));
    public static final Block ROCKS = new BaseVariantsBlockVar3(FabricBlockSettings.copyOf(Blocks.GRASS).nonOpaque(), Block.createCuboidShape(0, 0, 0, 16, 1, 16));
    public static final Block STICKS = new BaseVariantsBlockVar4(FabricBlockSettings.copyOf(Blocks.GRASS).nonOpaque(), Block.createCuboidShape(0, 0, 0, 16, 1, 16));
    public static final Block COBBLE_WALL = new Block(FabricBlockSettings.copyOf(Blocks.COBBLESTONE));

    public static final Block STONE_PILLAR = new StonePillarBlock(FabricBlockSettings.of(Material.STONE));
    public static final Block CRATE = new CrateBlock(FabricBlockSettings.copyOf(Blocks.CHEST));

    //DWARF FORGE
    public static final Block DWARF_FORGE_PIT = new DwarfForgePitBlock();
    public static final Block DWARF_FORGE_WALL = new DwarfForgeWallBlock();
    public static final Block DWARF_FORGE_CHIMNEY_WALL = new DwarfForgeChimneyBlock();
    public static final Block DWARF_FORGE_PILLAR = new DwarfForgePillarBlock();

    //CHAIRS
    public static final Block ACACIA_WOODEN_CHAIR_1 = createChair(VoxelShapeGroups.Chairs.CHAIR_1);
    public static final Block ACACIA_WOODEN_CHAIR_2 = createChair(VoxelShapeGroups.Chairs.CHAIR_2);
    public static final Block ACACIA_WOODEN_CHAIR_3 = createChair(VoxelShapeGroups.Chairs.CHAIR_3);
    public static final Block ACACIA_WOODEN_CHAIR_4 = createChair(VoxelShapeGroups.Chairs.CHAIR_4);
    public static final Block ACACIA_WOODEN_CHAIR_5 = createChair(VoxelShapeGroups.Chairs.CHAIR_5);

    public static final Block BIRCH_WOODEN_CHAIR_1 = createChair(VoxelShapeGroups.Chairs.CHAIR_1);
    public static final Block BIRCH_WOODEN_CHAIR_2 = createChair(VoxelShapeGroups.Chairs.CHAIR_2);
    public static final Block BIRCH_WOODEN_CHAIR_3 = createChair(VoxelShapeGroups.Chairs.CHAIR_3);
    public static final Block BIRCH_WOODEN_CHAIR_4 = createChair(VoxelShapeGroups.Chairs.CHAIR_4);
    public static final Block BIRCH_WOODEN_CHAIR_5 = createChair(VoxelShapeGroups.Chairs.CHAIR_5);

    public static final Block DARK_OAK_WOODEN_CHAIR_1 = createChair(VoxelShapeGroups.Chairs.CHAIR_1);
    public static final Block DARK_OAK_WOODEN_CHAIR_2 = createChair(VoxelShapeGroups.Chairs.CHAIR_2);
    public static final Block DARK_OAK_WOODEN_CHAIR_3 = createChair(VoxelShapeGroups.Chairs.CHAIR_3);
    public static final Block DARK_OAK_WOODEN_CHAIR_4 = createChair(VoxelShapeGroups.Chairs.CHAIR_4);
    public static final Block DARK_OAK_WOODEN_CHAIR_5 = createChair(VoxelShapeGroups.Chairs.CHAIR_5);

    public static final Block JUNGLE_WOODEN_CHAIR_1 = createChair(VoxelShapeGroups.Chairs.CHAIR_1);
    public static final Block JUNGLE_WOODEN_CHAIR_2 = createChair(VoxelShapeGroups.Chairs.CHAIR_2);
    public static final Block JUNGLE_WOODEN_CHAIR_3 = createChair(VoxelShapeGroups.Chairs.CHAIR_3);
    public static final Block JUNGLE_WOODEN_CHAIR_4 = createChair(VoxelShapeGroups.Chairs.CHAIR_4);
    public static final Block JUNGLE_WOODEN_CHAIR_5 = createChair(VoxelShapeGroups.Chairs.CHAIR_5);

    public static final Block OAK_WOODEN_CHAIR_1 = createChair(VoxelShapeGroups.Chairs.CHAIR_1);
    public static final Block OAK_WOODEN_CHAIR_2 = createChair(VoxelShapeGroups.Chairs.CHAIR_2);
    public static final Block OAK_WOODEN_CHAIR_3 = createChair(VoxelShapeGroups.Chairs.CHAIR_3);
    public static final Block OAK_WOODEN_CHAIR_4 = createChair(VoxelShapeGroups.Chairs.CHAIR_4);
    public static final Block OAK_WOODEN_CHAIR_5 = createChair(VoxelShapeGroups.Chairs.CHAIR_5);

    public static final Block SPRUCE_WOODEN_CHAIR_1 = createChair(VoxelShapeGroups.Chairs.CHAIR_1);
    public static final Block SPRUCE_WOODEN_CHAIR_2 = createChair(VoxelShapeGroups.Chairs.CHAIR_2);
    public static final Block SPRUCE_WOODEN_CHAIR_3 = createChair(VoxelShapeGroups.Chairs.CHAIR_3);
    public static final Block SPRUCE_WOODEN_CHAIR_4 = createChair(VoxelShapeGroups.Chairs.CHAIR_4);
    public static final Block SPRUCE_WOODEN_CHAIR_5 = createChair(VoxelShapeGroups.Chairs.CHAIR_5);

    public static final Block ACACIA_BENCH_1 = new BaseDoubleHorizontalBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), VoxelShapeGroups.BENCH_1_LEFT, VoxelShapeGroups.BENCH_1_RIGHT);
    public static final Block BIRCH_BENCH_1 = new BaseDoubleHorizontalBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), VoxelShapeGroups.BENCH_1_LEFT, VoxelShapeGroups.BENCH_1_RIGHT);
    public static final Block DARK_OAK_BENCH_1 = new BaseDoubleHorizontalBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), VoxelShapeGroups.BENCH_1_LEFT, VoxelShapeGroups.BENCH_1_RIGHT);
    public static final Block JUNGLE_BENCH_1 = new BaseDoubleHorizontalBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), VoxelShapeGroups.BENCH_1_LEFT, VoxelShapeGroups.BENCH_1_RIGHT);
    public static final Block OAK_BENCH_1 = new BaseDoubleHorizontalBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), VoxelShapeGroups.BENCH_1_LEFT, VoxelShapeGroups.BENCH_1_RIGHT);
    public static final Block SPRUCE_BENCH_1 = new BaseDoubleHorizontalBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS), VoxelShapeGroups.BENCH_1_LEFT, VoxelShapeGroups.BENCH_1_RIGHT);

    public static void register() {
        NorseCraftMod.LOGGER.info("Register blocks");
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
        registerBlock("bricks_blue", BRICKS_BLUEISH);
        registerBlock("bricks_gray", BRICKS_GRAY);
        registerBlock("rocks", ROCKS);
        registerBlock("cobble_wall", COBBLE_WALL);

        registerBlock("stone_pillar", STONE_PILLAR);
        registerBlock("crate", CRATE);
        registerBlock("sticks", STICKS);

        registerBlock("dwarf_forge_pit", DWARF_FORGE_PIT);
        registerBlock("dwarf_forge_wall", DWARF_FORGE_WALL);
        registerBlock("dwarf_forge_chimney_wall", DWARF_FORGE_CHIMNEY_WALL);
        registerBlock("dwarf_forge_pillar", DWARF_FORGE_PILLAR);

        registerBlock("acacia_wooden_chair_1", ACACIA_WOODEN_CHAIR_1);
        registerBlock("acacia_wooden_chair_2", ACACIA_WOODEN_CHAIR_2);
        registerBlock("acacia_wooden_chair_3", ACACIA_WOODEN_CHAIR_3);
        registerBlock("acacia_wooden_chair_4", ACACIA_WOODEN_CHAIR_4);
        registerBlock("acacia_wooden_chair_5", ACACIA_WOODEN_CHAIR_5);

        registerBlock("birch_wooden_chair_1", BIRCH_WOODEN_CHAIR_1);
        registerBlock("birch_wooden_chair_2", BIRCH_WOODEN_CHAIR_2);
        registerBlock("birch_wooden_chair_3", BIRCH_WOODEN_CHAIR_3);
        registerBlock("birch_wooden_chair_4", BIRCH_WOODEN_CHAIR_4);
        registerBlock("birch_wooden_chair_5", BIRCH_WOODEN_CHAIR_5);

        registerBlock("dark_oak_wooden_chair_1", DARK_OAK_WOODEN_CHAIR_1);
        registerBlock("dark_oak_wooden_chair_2", DARK_OAK_WOODEN_CHAIR_2);
        registerBlock("dark_oak_wooden_chair_3", DARK_OAK_WOODEN_CHAIR_3);
        registerBlock("dark_oak_wooden_chair_4", DARK_OAK_WOODEN_CHAIR_4);
        registerBlock("dark_oak_wooden_chair_5", DARK_OAK_WOODEN_CHAIR_5);

        registerBlock("jungle_wooden_chair_1", JUNGLE_WOODEN_CHAIR_1);
        registerBlock("jungle_wooden_chair_2", JUNGLE_WOODEN_CHAIR_2);
        registerBlock("jungle_wooden_chair_3", JUNGLE_WOODEN_CHAIR_3);
        registerBlock("jungle_wooden_chair_4", JUNGLE_WOODEN_CHAIR_4);
        registerBlock("jungle_wooden_chair_5", JUNGLE_WOODEN_CHAIR_5);

        registerBlock("oak_wooden_chair_1", OAK_WOODEN_CHAIR_1);
        registerBlock("oak_wooden_chair_2", OAK_WOODEN_CHAIR_2);
        registerBlock("oak_wooden_chair_3", OAK_WOODEN_CHAIR_3);
        registerBlock("oak_wooden_chair_4", OAK_WOODEN_CHAIR_4);
        registerBlock("oak_wooden_chair_5", OAK_WOODEN_CHAIR_5);

        registerBlock("spruce_wooden_chair_1", SPRUCE_WOODEN_CHAIR_1);
        registerBlock("spruce_wooden_chair_2", SPRUCE_WOODEN_CHAIR_2);
        registerBlock("spruce_wooden_chair_3", SPRUCE_WOODEN_CHAIR_3);
        registerBlock("spruce_wooden_chair_4", SPRUCE_WOODEN_CHAIR_4);
        registerBlock("spruce_wooden_chair_5", SPRUCE_WOODEN_CHAIR_5);

        registerBlock("acacia_bench_1", ACACIA_BENCH_1);
        registerBlock("birch_bench_1", BIRCH_BENCH_1);
        registerBlock("dark_oak_bench_1", DARK_OAK_BENCH_1);
        registerBlock("jungle_bench_1", JUNGLE_BENCH_1);
        registerBlock("oak_bench_1", OAK_BENCH_1);
        registerBlock("spruce_bench_1", SPRUCE_BENCH_1);
    }

    public static <T extends Block> void registerBlock(String name, T block) {
        Registry.register(Registry.BLOCK, new Identifier(NorseCraftMod.MOD_ID, name), block);
        registerBlockItem(name, block);
    }

    private static <T extends Block> void registerBlockItem(String name, T block) {
        Registry.register(Registry.ITEM, new Identifier(NorseCraftMod.MOD_ID, name), new BlockItem(block, new FabricItemSettings().group(NorseCraftMod.GROUP)));
    }

    private static Block createChair(VoxelShapeGroup group) {
        return new ChairBlock(CHAIR_PROPS, group);
    }

}
