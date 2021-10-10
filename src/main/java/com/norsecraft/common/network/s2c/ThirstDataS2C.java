package com.norsecraft.common.network.s2c;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.common.entity.NorseCraftPlayerEntity;
import com.norsecraft.common.network.INCPacket;
import com.norsecraft.common.thirst.ThirstManager;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

/**
 * Sever to Client Thirst data packet
 * sends information about the current water level and any damage/healing taken.
 */
public class ThirstDataS2C implements INCPacket {
  /**
   * Thirst data packet ID
   */
  public static final Identifier PACKET_ID = NorseCraftMod.packet("thirst_data");
  private final int waterLevel;
  private final int damageType; 
  private final float damage;

  /**
   * Create a new Thirst data packet with given values.
   * @param waterLevel water level value to send
   * @param damageType damage type to send
   * @param damage amount of damage to send
   */
  public ThirstDataS2C(int waterLevel, int damageType, float damage) {
    this.waterLevel = waterLevel;
    this.damageType = damageType;
    this.damage = damage;
  }

  /**
   * Create a new Thirst data packet with buffer.
   * @param buf buffer full of packet data
   */
  public ThirstDataS2C(PacketByteBuf buf) {
    this.waterLevel = buf.readInt();
    this.damageType = buf.readInt();
    this.damage = buf.readFloat();
  }

  @Override
  public PacketByteBuf write() {
    PacketByteBuf buf = PacketByteBufs.create();
    buf.writeInt(waterLevel);
    buf.writeInt(damageType);
    buf.writeFloat(damage);
    return buf;
  }
  
  /**
   * Handle on the client side data from the server.
   * @param client client to handle data with.
   * @param handler client network handler
   * @param buf buffer of packet data
   * @param responseSender packet response sender
   */
  public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
    
    // get data
    var thirstData = new ThirstDataS2C(buf);
    // if the player exists yet
    var player = (NorseCraftPlayerEntity) client.getCameraEntity();
    if(player == null) {
      return;
    }
    // update values on player.
    player.getThirstManager().setWaterLevel(thirstData.waterLevel);
    if(thirstData.damageType == ThirstManager.HEAL) {
      client.player.heal(thirstData.damage);
    } else if (thirstData.damageType == ThirstManager.DAMAGE) {
      client.player.damage(DamageSource.STARVE, thirstData.damage);
    }
  }
}
