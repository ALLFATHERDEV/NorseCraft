package com.norsecraft.common.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.Objects;

/**
 * A helper class for networking
 */
public class NetworkHelper {

    /**
     * This method sends a packet from the server to all clients
     *
     * @param world  the server world
     * @param id     the packet id
     * @param packet the packet object
     */
    public static void broadcastToClient(ServerWorld world, Identifier id, INCPacket packet) {
        Objects.requireNonNull(world, "world");
        Objects.requireNonNull(packet, "packet");
        world.getPlayers().forEach(spe -> ServerPlayNetworking.send(spe, id, packet.write()));
    }

}
