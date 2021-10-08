package com.norsecraft.common.block.entity;

import com.norsecraft.common.gui.CampfireGuiInterpretation;
import com.norsecraft.common.registry.NCBlockEntities;
import com.norsecraft.common.registry.NCRecipeTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class CampfireBlockEntity extends AbstractFurnaceBlockEntity implements IAnimatable {

    private AnimationFactory factory = new AnimationFactory(this);

    private boolean active;

    public CampfireBlockEntity(BlockPos pos, BlockState state) {
        super(NCBlockEntities.campfireBlockEntity, pos, state, NCRecipeTypes.campfire);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void registerControllers(AnimationData animationData) {

    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    protected Text getContainerName() {
        return Text.of("Campfire");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new CampfireGuiInterpretation(syncId, playerInventory, ScreenHandlerContext.create(this.world, this.pos), this.propertyDelegate);
    }


}
