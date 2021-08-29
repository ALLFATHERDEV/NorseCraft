package com.norsecraft;

import com.norsecraft.client.render.CustomBlockEntityModelRenderer;
import com.norsecraft.client.render.entity.DwarfEntityRenderer;
import com.norsecraft.client.render.entity.WildBoarEntityRenderer;
import com.norsecraft.client.render.model.block.CrateBlockModel;
import com.norsecraft.client.render.model.entity.DwarfBlacksmithEntityModel;
import com.norsecraft.client.render.model.entity.DwarfEntityModel;
import com.norsecraft.client.render.model.entity.DwarfWarriorEntityModel;
import com.norsecraft.client.render.model.entity.DwarfWiseEntityModel;
import com.norsecraft.client.screen.CrateBlockScreen;
import com.norsecraft.client.screen.DwarfTradeScreen;
import com.norsecraft.client.screen.NorseCraftInventoryScreen;
import com.norsecraft.common.entity.dwarf.AbstractDwarfEntity;
import com.norsecraft.common.registry.NCBlockEntities;
import com.norsecraft.common.registry.NCEntities;
import com.norsecraft.common.registry.NCScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.entity.EntityType;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class NorseCraftModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        //========================================================================
        //=============================ENTITY RENDERER============================
        //========================================================================
        EntityRendererRegistry.register(NCEntities.wildBoar, WildBoarEntityRenderer::new);

        //Dwarfs
        registerDwarf(NCEntities.dwarf, null, new DwarfEntityModel());
        registerDwarf(NCEntities.dwarfBlacksmith, "entity/dwarf_blacksmith.png", new DwarfBlacksmithEntityModel());
        registerDwarf(NCEntities.dwarfWise, "entity/dwarf_wise.png", new DwarfWiseEntityModel());
        registerDwarf(NCEntities.dwarfWarrior, "entity/dwarf_warrior1_var2.png", new DwarfWarriorEntityModel());

        //========================================================================
        //==============================BLOCK RENDERER============================
        //========================================================================
        BlockEntityRendererRegistry.register(NCBlockEntities.crateBlockEntity, ctx -> new CustomBlockEntityModelRenderer<>(new CrateBlockModel()));

        //========================================================================
        //================================SCREENS=================================
        //========================================================================
        ScreenRegistry.register(NCScreenHandlers.dwarfTrade, DwarfTradeScreen::new);
        ScreenRegistry.register(NCScreenHandlers.norseCraftInventory, NorseCraftInventoryScreen::new);
        ScreenRegistry.register(NCScreenHandlers.crate, CrateBlockScreen::new);
    }

    private <T extends AbstractDwarfEntity> void registerDwarf(EntityType<T> entity, String texturePath, AnimatedGeoModel<T> model) {
        EntityRendererRegistry.register(entity, (ctx) -> new DwarfEntityRenderer<>(ctx, model, NorseCraftMod.ncTex(texturePath)));
    }

}
