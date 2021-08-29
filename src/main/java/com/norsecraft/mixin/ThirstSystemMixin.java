package com.norsecraft.mixin;

import com.norsecraft.common.thirst.ThirstManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class ThirstSystemMixin {

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeThirstManagerNBT(NbtCompound nbt, CallbackInfo info) {
        ThirstManager.THIRST_MANAGER.write(nbt);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readThirstManagerNBT(NbtCompound nbt, CallbackInfo info) {
        ThirstManager.THIRST_MANAGER.read(nbt);
    }

}
