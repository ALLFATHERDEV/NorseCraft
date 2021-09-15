package com.norsecraft.common.network.c2s;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.network.INCPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class OpenScreenPacketC2S implements INCPacket {

    public static final Identifier OPEN_SCREEN_PACKET_ID = NorseCraftMod.packet("open_screen_packet");

    private final int entityId;

    public OpenScreenPacketC2S(int entityId) {
        this.entityId = entityId;
    }

    public OpenScreenPacketC2S(PacketByteBuf buf) {
        this.entityId = buf.readVarInt();
    }

    @Override
    public PacketByteBuf write() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(this.entityId);
        return buf;
    }

}
