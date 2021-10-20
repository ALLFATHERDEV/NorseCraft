package com.norsecraft;

import com.norsecraft.client.gui.NorseCraftInventoryScreen;
import com.norsecraft.client.gui.dwarf.DwarfTradeGuiInterpretation;
import com.norsecraft.client.render.CustomBlockEntityModelRenderer;
import com.norsecraft.client.render.entity.BaseEntityRenderer;
import com.norsecraft.client.render.entity.DwarfEntityRenderer;
import com.norsecraft.client.render.model.block.CrateBlockModel;
import com.norsecraft.client.render.model.entity.*;
import com.norsecraft.client.render.model.joint.JointModel;
import com.norsecraft.client.render.model.joint.JointModelRenderer;
import com.norsecraft.client.render.model.joint.JointStateHandler;
import com.norsecraft.client.ymir.screen.YmirInventoryScreen;
import com.norsecraft.common.entity.dwarf.AbstractDwarfEntity;
import com.norsecraft.common.gui.CampfireGuiInterpretation;
import com.norsecraft.common.gui.CrateGuiInterpretation;
import com.norsecraft.common.registry.NCBlockEntities;
import com.norsecraft.common.registry.NCBlocks;
import com.norsecraft.common.registry.NCEntities;
import com.norsecraft.common.registry.NCScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

/**
 * Main mod class for the client
 */
public class NorseCraftModClient implements ClientModInitializer {

    public static final boolean DATA_GEN = true;

    @Override
    public void onInitializeClient() {
        NorseCraftMod.LOGGER.info("Register entity renderer");
        //========================================================================
        //=============================ENTITY RENDERER============================
        //========================================================================
        registerBaseEntity(NCEntities.brownBear, new BrownBearEntityModel(), BaseEntityRenderer::new);
        registerBaseEntity(NCEntities.wildBoar, new WildBoarEntityModel(), BaseEntityRenderer::new);

        //Dwarfs
        registerDwarf(NCEntities.dwarf, null, new DwarfEntityModel());
        registerDwarf(NCEntities.dwarfBlacksmith, "entity/dwarf_blacksmith.png", new DwarfBlacksmithEntityModel());
        registerDwarf(NCEntities.dwarfWise, "entity/dwarf_wise.png", new DwarfWiseEntityModel());
        registerDwarf(NCEntities.dwarfWarrior, "entity/dwarf_warrior1_var2.png", new DwarfWarriorEntityModel());

        NorseCraftMod.LOGGER.info("Register block renderer");
        //========================================================================
        //==============================BLOCK RENDERER============================
        //========================================================================
        BlockEntityRendererRegistry.register(NCBlockEntities.crateBlockEntity, ctx -> new CustomBlockEntityModelRenderer<>(new CrateBlockModel()));
        this.registerJointModelRenderer(NCBlockEntities.campfireBlockEntity);

        BlockRenderLayerMap.INSTANCE.putBlock(NCBlocks.ROCKS, RenderLayer.getCutout());

        NorseCraftMod.LOGGER.info("Register screens");
        //========================================================================
        //================================SCREENS=================================
        //========================================================================
        ScreenRegistry.register(NCScreenHandlers.norseCraftInventory, NorseCraftInventoryScreen::new);

        ScreenRegistry.<CampfireGuiInterpretation, YmirInventoryScreen<CampfireGuiInterpretation>>register(NCScreenHandlers.campfireGuiBasic,
                (desc, inventory, title) -> new YmirInventoryScreen<>(desc, inventory.player, title));
        ScreenRegistry.<DwarfTradeGuiInterpretation, YmirInventoryScreen<DwarfTradeGuiInterpretation>>register(NCScreenHandlers.dwarfTrade,
                (desc, inventory, title) -> new YmirInventoryScreen<>(desc, inventory.player, title));
        ScreenRegistry.<CrateGuiInterpretation, YmirInventoryScreen<CrateGuiInterpretation>>register(NCScreenHandlers.crate,
                (desc, inventory, title) -> new YmirInventoryScreen<>(desc, inventory.player, title));

    }

    private <T extends AbstractDwarfEntity> void registerDwarf(EntityType<T> entity, String texturePath, AnimatedGeoModel<T> model) {
        EntityRendererRegistry.register(entity, (ctx) -> new DwarfEntityRenderer<>(ctx, model, NorseCraftMod.ncTex(texturePath)));
    }

    private <T extends LivingEntity & IAnimatable> void registerBaseEntity(EntityType<T> entityType, AnimatedGeoModel<T> model, BaseFactory factory) {
        EntityRendererRegistry.register(entityType, (ctx) -> factory.build(ctx, model));
    }

    /**
     * If you have a block with a jointed model you have to register it, with this method
     * The {@link JointModelRenderer} takes automatic the {@link JointModel} from the block entity
     *
     * @param blockEntityType the block entity type
     * @param <T> type
     */
    private <T extends BlockEntity & IAnimatable & JointStateHandler<T>> void registerJointModelRenderer(BlockEntityType<T> blockEntityType) {
        BlockEntityRendererRegistry.register(blockEntityType, ctx -> new JointModelRenderer<>());
    }

    private interface BaseFactory {

        <T extends LivingEntity & IAnimatable> EntityRenderer<T> build(EntityRendererFactory.Context ctx, AnimatedGeoModel<T> model);

    }

}
