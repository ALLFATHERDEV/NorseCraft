package com.norsecraft.common.network.c2s;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.ymir.interpretation.MerchantGuiInterpretation;
import com.norsecraft.common.network.INCPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class SelectMerchantRecipeIndexPacketC2S implements INCPacket {

    public static final Identifier SELECT_MERCHANT_RECIPE_INDEX = NorseCraftMod.packet("select_merchant_recipe_index");

    private final int tradeId;

    public SelectMerchantRecipeIndexPacketC2S(int tradeId) {
        this.tradeId = tradeId;
    }

    public SelectMerchantRecipeIndexPacketC2S(PacketByteBuf buf) {
        this.tradeId = buf.readInt();
    }

    @Override
    public PacketByteBuf write() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(tradeId);
        return buf;
    }

    public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int i = buf.readInt();
        ScreenHandler screenHandler = player.currentScreenHandler;
        if(screenHandler instanceof MerchantGuiInterpretation) {
            MerchantGuiInterpretation sh = (MerchantGuiInterpretation) screenHandler;
            sh.setOfferIndex(i);
            sh.switchTo(i);
        }
    }

}
