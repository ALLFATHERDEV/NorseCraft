package com.norsecraft.common.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class NetworkHelper {

    public static void broadcastToClient(ServerWorld world, Identifier id, INCPacket packet) {
        Objects.requireNonNull(world, "world");
        Objects.requireNonNull(packet, "packet");
        world.getPlayers().forEach(spe -> ServerPlayNetworking.send(spe, id, packet.write()));
    }

}
