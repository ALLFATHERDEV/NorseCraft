package com.norsecraft.common.registry;

import com.norsecraft.common.block.entity.CrateBlockEntity;
import com.norsecraft.common.block.entity.DwarfForgeBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class NCBlockEntities {

    public static BlockEntityType<CrateBlockEntity> crateBlockEntity;
    public static BlockEntityType<DwarfForgeBlockEntity> dwarfForgeEntity;

    public static void register() {
        crateBlockEntity = register("crate_be", CrateBlockEntity::new, NCBlocks.CRATE);
        dwarfForgeEntity = register("dwarf_forge", DwarfForgeBlockEntity::new, NCBlocks.DWARF_FORGE_PIT, NCBlocks.DWARF_FORGE_PILLAR, NCBlocks.DWARF_FORGE_CHIMNEY_WALL, NCBlocks.DWARF_FORGE_WALL);
    }

    private static <T extends BlockEntity>  BlockEntityType<T> register(String name, FabricBlockEntityTypeBuilder.Factory<T> factory, Block... blocks) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, name, FabricBlockEntityTypeBuilder.create(factory, blocks).build(null));
    }

}
