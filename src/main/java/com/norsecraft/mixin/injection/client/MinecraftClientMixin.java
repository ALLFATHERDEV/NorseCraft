package com.norsecraft.mixin.injection.client;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.NorseCraftModClient;
import com.norsecraft.client.gui.NorseCraftInventoryScreen;
import com.norsecraft.common.screenhandler.NorseCraftInventoryScreenHandler;
import com.norsecraft.datagen.NorseCraftDataGenerator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.text.LiteralText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Shadow
    @Final
    private ReloadableResourceManager resourceManager;

    @Inject(method = "handleInputEvents()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/TutorialManager;onInventoryOpened()V"), cancellable = true)
    private void renderOwnInventory(CallbackInfo info) {
        MinecraftClient client = MinecraftClient.getInstance();
        NorseCraftInventoryScreenHandler handler = new NorseCraftInventoryScreenHandler(0, client.player.getInventory());
        NorseCraftInventoryScreen screen = new NorseCraftInventoryScreen(handler, client.player.getInventory(), LiteralText.EMPTY);
        client.setScreen(screen);
        NorseCraftMod.LOGGER.info("Injected... opening own inventory");
        info.cancel();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void loadDialogs(RunArgs args, CallbackInfo info) {
        NorseCraftMod.getDialogManager().loadDialogs(this.resourceManager);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void dataGen(RunArgs args, CallbackInfo info) {
        if(NorseCraftModClient.DATA_GEN) {
            Logger logger = LogManager.getLogger("NorseCraft Data gen");
            logger.info("STARTING DATA GENERATING");

            for(NorseCraftDataGenerator dataGen : NorseCraftDataGenerator.DATA_GENERATORS) {
                logger.info("GENERATING: {}", dataGen.getDataGeneratorName());
                dataGen.generate();
            }

            logger.info("STOPPING DATA GENERATING");
            System.exit(0);
        }
    }


}
