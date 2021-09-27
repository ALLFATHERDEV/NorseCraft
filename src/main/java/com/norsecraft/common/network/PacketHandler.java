package com.norsecraft.common.network;

import com.norsecraft.common.network.c2s.OpenScreenPacketC2S;
import com.norsecraft.common.network.c2s.SelectMerchantRecipeIndexPacketC2S;
import com.norsecraft.common.network.s2c.SendAttackingEntityS2C;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class PacketHandler {

    public static void handleClientToServerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(SelectMerchantRecipeIndexPacketC2S.SELECT_MERCHANT_RECIPE_INDEX, SelectMerchantRecipeIndexPacketC2S::handle);
        ServerPlayNetworking.registerGlobalReceiver(OpenScreenPacketC2S.OPEN_SCREEN_PACKET_ID, OpenScreenPacketC2S::handle);
    }

    public static void handleServerToClientPackets() {
        ClientPlayNetworking.registerGlobalReceiver(SendAttackingEntityS2C.ID, SendAttackingEntityS2C::handle);
    }

}
