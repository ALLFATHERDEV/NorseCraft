package com.norsecraft.common.network.c2s;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.entity.dwarf.DwarfBlacksmithEntity;
import com.norsecraft.common.network.INCPacket;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

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

    public static void handle(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Entity entity = player.world.getEntityById(buf.readVarInt());
        if (entity instanceof DwarfBlacksmithEntity) {
            DwarfBlacksmithEntity dwarf = (DwarfBlacksmithEntity) entity;
            ExtendedScreenHandlerFactory extendedScreenHandlerFactory = new ExtendedScreenHandlerFactory() {
                @Override
                public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                    buf.writeInt(entity.getId());
                }

                @Override
                public Text getDisplayName() {
                    return new LiteralText("Dwarf Trade");
                }

                @Nullable
                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return dwarf.createMenu(syncId, inv, player);
                }
            };
            dwarf.setCurrentCustomer(player);
            player.openHandledScreen(extendedScreenHandlerFactory);
        }
    }

}
