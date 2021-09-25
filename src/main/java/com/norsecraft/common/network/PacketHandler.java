package com.norsecraft.common.network;

import com.norsecraft.common.entity.dwarf.DwarfBlacksmithEntity;
import com.norsecraft.common.network.c2s.OpenScreenPacketC2S;
import com.norsecraft.common.network.c2s.SelectMerchantRecipeIndexPacketC2S;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class PacketHandler {

    public static void handleClientToServerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(SelectMerchantRecipeIndexPacketC2S.SELECT_MERCHANT_RECIPE_INDEX, (server, player, handler, buf, responseSender) -> {
            int i = buf.readInt();
            ScreenHandler screenHandler = player.currentScreenHandler;
            /*if (screenHandler instanceof DwarfTradeScreenHandler) {
                DwarfTradeScreenHandler sh = (DwarfTradeScreenHandler) screenHandler;
                sh.setCurrentRecipeIndex(i);
                sh.switchTo(i);
            }*/
        });

        ServerPlayNetworking.registerGlobalReceiver(OpenScreenPacketC2S.OPEN_SCREEN_PACKET_ID, ((server, player, handler, buf, responseSender) -> {
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
        }));

    }

}
