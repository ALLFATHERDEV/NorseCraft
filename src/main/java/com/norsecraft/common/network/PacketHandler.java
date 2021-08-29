package com.norsecraft.common.network;

import com.norsecraft.common.network.c2s.SelectMerchantRecipeIndexPacketC2S;
import com.norsecraft.common.screenhandler.DwarfTradeScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.screen.ScreenHandler;

public class PacketHandler {

    public static void handleClientToServerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(SelectMerchantRecipeIndexPacketC2S.SELECT_MERCHANT_RECIPE_INDEX, (server, player, handler, buf, responseSender) -> {
            int i = buf.readInt();
            ScreenHandler screenHandler = player.currentScreenHandler;
            if(screenHandler instanceof DwarfTradeScreenHandler) {
                DwarfTradeScreenHandler sh = (DwarfTradeScreenHandler) screenHandler;
                sh.setCurrentRecipeIndex(i);
                sh.switchTo(i);
            }
        });
    }

}
