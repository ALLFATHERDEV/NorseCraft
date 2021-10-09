package com.norsecraft.common.registry;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.entity.animal.BrownBearEntity;
import com.norsecraft.common.entity.animal.WildBoarEntity;
import com.norsecraft.common.entity.dwarf.DwarfBlacksmithEntity;
import com.norsecraft.common.entity.dwarf.DwarfEntity;
import com.norsecraft.common.entity.dwarf.DwarfWarriorEntity;
import com.norsecraft.common.entity.dwarf.DwarfWiseEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Register all entities from the mod
 */
public class NCEntities {

    public static EntityType<WildBoarEntity> wildBoar;
    public static EntityType<DwarfEntity> dwarf;
    public static EntityType<DwarfBlacksmithEntity> dwarfBlacksmith;
    public static EntityType<DwarfWiseEntity> dwarfWise;
    public static EntityType<DwarfWarriorEntity> dwarfWarrior;
    public static EntityType<BrownBearEntity> brownBear;

    public static void register() {
        NorseCraftMod.LOGGER.info("Register entities");
        wildBoar        = registerEntity("wild_boar", WildBoarEntity::new, SpawnGroup.CREATURE, 1.3f, 1f);
        brownBear       = registerEntity("brown_bear", BrownBearEntity::new, SpawnGroup.CREATURE, 1.5F, 1.5F);

        //Dwarfs
        dwarf           = registerDwarf("dwarf", DwarfEntity::new);
        dwarfBlacksmith = registerDwarf("dwarf_blacksmith", DwarfBlacksmithEntity::new);
        dwarfWise       = registerDwarf("dwarf_wise", DwarfWiseEntity::new);
        dwarfWarrior    = registerDwarf("dwarf_warrior", DwarfWarriorEntity::new);

    }

    private static <T extends Entity> EntityType<T> registerEntity(String name, EntityType.EntityFactory<T> factory, SpawnGroup group, float widht, float height) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(NorseCraftMod.MOD_ID, name),
                FabricEntityTypeBuilder.create(group, factory).dimensions(EntityDimensions.fixed(widht, height)).build());
    }

    private static <T extends Entity> EntityType<T> registerDwarf(String name, EntityType.EntityFactory<T> factory) {
        return registerEntity(name, factory, SpawnGroup.CREATURE, 1.1F, 1.5F);
    }

}
