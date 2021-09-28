package com.norsecraft.common.block.entity;

import com.norsecraft.client.ymir.test.ImplementedInventory;
import com.norsecraft.common.gui.CrateGuiInterpretation;
import com.norsecraft.common.registry.NCBlockEntities;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class CrateBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory, IAnimatable {

    private static final String OPENING_ANIMATION = "animation.crate_open";
    private static final String CLOSING_ANIMATION = "animation.crate_close";
    private static final String IDLE_ANIMATION = "animation.crate_idle";

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
    private final AnimationFactory factory = new AnimationFactory(this);

    private boolean open;
    private boolean playOpenAnimation;
    private boolean playCloseAnimation;

    public CrateBlockEntity(BlockPos pos, BlockState state) {
        super(NCBlockEntities.crateBlockEntity, pos, state);
    }

    @Override
    public Text getDisplayName() {
        return new LiteralText("Crate block");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new CrateGuiInterpretation(syncId, inv, ScreenHandlerContext.create(this.world, this.pos), this);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
    }

    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (playOpenAnimation) {
            playOpenAnimation = false;
            event.getController().setAnimation(new AnimationBuilder().addAnimation(OPENING_ANIMATION));
        } else if (playCloseAnimation) {
            playCloseAnimation = false;
            event.getController().setAnimation(new AnimationBuilder().addAnimation(CLOSING_ANIMATION));
        } else if (open) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(IDLE_ANIMATION, false));
        }
        return PlayState.CONTINUE;
    }

    public void setOpen(boolean open) {
        this.open = open;
        if (this.open) {
            this.playOpenAnimation = true;
        } else {
            this.playCloseAnimation = true;
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }
}
