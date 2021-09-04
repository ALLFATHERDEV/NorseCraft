package com.norsecraft.mixin.injection.common;

import com.norsecraft.common.thirst.ThirstManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickThirstManager(CallbackInfo info) {
        ThirstManager.THIRST_MANAGER.tick(MinecraftClient.getInstance().player);
    }

}
