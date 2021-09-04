package com.norsecraft.mixin.injection.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.render.TextureSprite;
import com.norsecraft.common.thirst.ThirstManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class RenderOverlayMixin {

    @Shadow
    @Final
    private MinecraftClient client;
    private static final Identifier THIRST_BAR_TEX = NorseCraftMod.ncTex("gui/thirst_bar.png");
    private static final TextureSprite[] THIRST_BAR_SPRITES = new TextureSprite[]{
            new TextureSprite(9, 0, 9, 8),
            new TextureSprite(18, 0, 9, 9),
            new TextureSprite(0, 0, 9, 9)
    };

    @Inject(method = "renderStatusBars", at = @At(value = "TAIL"))
    private void render(MatrixStack stack, CallbackInfo info) {
        if (this.client.interactionManager.hasCreativeInventory())
            return;
        RenderSystem.setShaderTexture(0, THIRST_BAR_TEX);
        MinecraftClient client = MinecraftClient.getInstance();
        int mainWindowWidth = client.getWindow().getScaledWidth();
        int mainWindowHeight = client.getWindow().getScaledHeight();
        ThirstManager manager = ThirstManager.THIRST_MANAGER;
        int thirstLevel = manager.getThirstLevel();
        int x = mainWindowWidth / 2 + 91;
        int j3 = client.player.isSubmergedIn(FluidTags.WATER) ? mainWindowHeight - 59 : mainWindowHeight - 49;

        stack.push();
        for (int i = 0; i < 10; i++) {
            int k8 = x - i * 8 - 9;
            TextureSprite full = THIRST_BAR_SPRITES[0];
            Screen.drawTexture(stack, k8, j3, full.x, full.y, full.width, full.height, 45, 9);
            if (i * 2 + 1 < thirstLevel) {
                TextureSprite half = THIRST_BAR_SPRITES[2];
                Screen.drawTexture(stack, k8, j3, half.x, half.y, half.width, half.height, 45, 9);
            }

            if (i * 2 + 1 == thirstLevel) {
                TextureSprite empty = THIRST_BAR_SPRITES[1];
                Screen.drawTexture(stack, k8, j3, empty.x, empty.y, empty.width, empty.height, 45, 9);
            }
        }
        stack.pop();
    }

}
