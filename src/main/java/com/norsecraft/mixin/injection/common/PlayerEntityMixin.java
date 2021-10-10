package com.norsecraft.mixin.injection.common;

import com.norsecraft.common.entity.NorseCraftPlayerEntity;
import com.norsecraft.common.thirst.ThirstManager;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements NorseCraftPlayerEntity {
    public final ThirstManager thirstManager = new ThirstManager();

    public ThirstManager getThirstManager() {
        return thirstManager;
    }

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeThirstManagerNBT(NbtCompound nbt, CallbackInfo info) {
        thirstManager.write(nbt);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readThirstManagerNBT(NbtCompound nbt, CallbackInfo info) {
        thirstManager.read(nbt);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void customTick() {
        if (!world.isClient) {
            thirstManager.update((PlayerEntity) (LivingEntity) this);
        }
    }
}
