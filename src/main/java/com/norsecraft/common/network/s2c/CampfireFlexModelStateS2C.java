package com.norsecraft.common.network.s2c;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.block.entity.CampfireBlockEntity;
import com.norsecraft.common.network.INCPacket;
import com.norsecraft.common.util.BlockEntityUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

/**
 * Sends the current model state to the client
 */
public class CampfireFlexModelStateS2C implements INCPacket {

    public static final Identifier PACKET_ID = NorseCraftMod.packet("campfire_model_state");

    /**
     * The position from the block entity
     */
    private final BlockPos blockEntityPos;

    /**
     * The current model state
     */
    private final CampfireBlockEntity.State state;

    public CampfireFlexModelStateS2C(BlockPos blockEntityPos, CampfireBlockEntity.State state) {
        this.blockEntityPos = blockEntityPos;
        this.state = state;
    }

    public CampfireFlexModelStateS2C(PacketByteBuf buf) {
        this.blockEntityPos = buf.readBlockPos();
        this.state = buf.readEnumConstant(CampfireBlockEntity.State.class);
    }

    @Override
    public PacketByteBuf write() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(this.blockEntityPos);
        buf.writeEnumConstant(this.state);
        return buf;
    }

    public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        CampfireBlockEntity.State state = buf.readEnumConstant(CampfireBlockEntity.State.class);
        CampfireBlockEntity cbe = BlockEntityUtil.getBlockEntity(CampfireBlockEntity.class, handler.getWorld(), pos);
        cbe.setModelState(state);
    }

}
