package com.norsecraft.common.network.c2s;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.network.INCPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
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
}
