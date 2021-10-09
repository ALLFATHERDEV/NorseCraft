package com.norsecraft.common.network;

import net.minecraft.network.PacketByteBuf;

/**
 * A norsecraft packet class every packet should implement this interface
 */
public interface INCPacket {

    PacketByteBuf write();

}
