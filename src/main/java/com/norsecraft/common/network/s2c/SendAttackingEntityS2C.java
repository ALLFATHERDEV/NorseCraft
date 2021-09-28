package com.norsecraft.common.network.s2c;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.entity.animal.ClientAttackAnimationProvider;
import com.norsecraft.common.network.INCPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class SendAttackingEntityS2C implements INCPacket {

    public static final Identifier ID = NorseCraftMod.packet("send_attacking_entity_s2c");

    public static final int NONE = -1;

    private final int status;
    private final int entityId;
    private final int targetEntityId;

    public SendAttackingEntityS2C(int entityId, int targetEntityId, int status) {
        this.entityId = entityId;
        this.status = status;
        this.targetEntityId = targetEntityId;
    }

    public SendAttackingEntityS2C(int entityId, int targetEntityId) {
        this(entityId, targetEntityId, 0);
    }

    public SendAttackingEntityS2C(PacketByteBuf buf) {
        this.entityId = buf.readInt();
        this.status = buf.readInt();
        this.targetEntityId = buf.readInt();
    }

    @Override
    public PacketByteBuf write() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(entityId);
        buf.writeInt(status);
        buf.writeInt(targetEntityId);
        return buf;
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Entity entity = handler.getWorld().getEntityById(buf.readInt());
        int status = buf.readInt();
        Entity target = status == NONE ? null : handler.getWorld().getEntityById(buf.readInt());
        if(entity instanceof ClientAttackAnimationProvider) {
            if (target == null) {
                ((ClientAttackAnimationProvider) entity).setCurrentAttackerOnClient(null);
            } else {
                ((ClientAttackAnimationProvider) entity).setCurrentAttackerOnClient((LivingEntity) target);
            }
        }

    }

}
