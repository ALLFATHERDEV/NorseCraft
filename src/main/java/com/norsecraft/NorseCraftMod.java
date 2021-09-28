package com.norsecraft;

import com.norsecraft.common.block.dwarfforge.DwarfForgeMultiblock;
import com.norsecraft.common.block.multiblock.MultiblockManager;
import com.norsecraft.common.dialog.DialogManager;
import com.norsecraft.common.entity.animal.BrownBearEntity;
import com.norsecraft.common.entity.animal.WildBoarEntity;
import com.norsecraft.common.entity.dwarf.AbstractDwarfEntity;
import com.norsecraft.common.entity.dwarf.DwarfBlacksmithEntity;
import com.norsecraft.common.entity.dwarf.DwarfWarriorEntity;
import com.norsecraft.common.network.PacketHandler;
import com.norsecraft.common.registry.*;
import com.norsecraft.common.thirst.ThirstManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.OverworldBiomes;
import net.fabricmc.fabric.api.biome.v1.OverworldClimate;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

public class NorseCraftMod implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger(NorseCraftMod.class);
    public static final String MOD_ID = "norsecraft";

    private static MultiblockManager multiblockManager;
    private static final DialogManager dialogManager = new DialogManager();

    @Override
    public void onInitialize() {
        LOGGER.info("Starting mod");
        multiblockManager = new MultiblockManager();
        LOGGER.info("Initializing geckolib");
        GeckoLib.initialize();

        NCBlocks.register();
        NCItems.register();
        NCEntities.register();
        NCScreenHandlers.register();
        NCBlockEntities.register();
        NCFeatures.register();
        NCConfiguredFeatures.register();
        NCBiomes.register();

        LOGGER.info("Loading multiblocks");
        multiblockManager.addMultiblock(new DwarfForgeMultiblock());

        LOGGER.info("Register entity attributes");
        this.registerEntityAttributes();

        LOGGER.info("Create packethandler");
        PacketHandler.handleClientToServerPackets();
        PacketHandler.handleServerToClientPackets();

        LootTableListener.listen();

        OverworldBiomes.addContinentalBiome(NCBiomes.DWARF_BIOME, OverworldClimate.TEMPERATE, 3D);
        OverworldBiomes.addContinentalBiome(NCBiomes.DWARF_BIOME, OverworldClimate.COOL, 3D);

        ThirstManager.handleWorldTick();
    }

    private void registerEntityAttributes() {
        FabricDefaultAttributeRegistry.register(NCEntities.wildBoar, WildBoarEntity.createWildBoarAttributes());
        FabricDefaultAttributeRegistry.register(NCEntities.dwarf, AbstractDwarfEntity.createDwarfAttributes());
        FabricDefaultAttributeRegistry.register(NCEntities.dwarfBlacksmith, DwarfBlacksmithEntity.createDwarfBlacksmithAttributes());
        FabricDefaultAttributeRegistry.register(NCEntities.dwarfWarrior, DwarfWarriorEntity.createDwarfWarriorAttributes());
        FabricDefaultAttributeRegistry.register(NCEntities.dwarfWise, AbstractDwarfEntity.createDwarfAttributes());
        FabricDefaultAttributeRegistry.register(NCEntities.brownBear, BrownBearEntity.createBrownBearAttributes());
    }

    public static MultiblockManager getMultiblockManager() {
        return multiblockManager;
    }

    public static Identifier nc(String name) {
        return new Identifier(MOD_ID, name);
    }

    public static Identifier ncTex(String name) {
        return new Identifier(MOD_ID, "textures/" + name);
    }

    public static Identifier geoModel(String name) {
        return new Identifier(MOD_ID, "geo/" + name);
    }

    public static Identifier packet(String name) {
        return new Identifier(MOD_ID, "packet_" + name);
    }

    public static final ItemGroup GROUP = FabricItemGroupBuilder
            .create(nc("nc_main"))
            .icon(() -> new ItemStack(Items.IRON_AXE)).build();

    public static DialogManager getDialogManager() {
        return dialogManager;
    }
}
