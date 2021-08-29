package com.norsecraft.common.thirst;

import com.norsecraft.NorseCraftMod;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;

public class ThirstManager {

    public static final ThirstManager THIRST_MANAGER = new ThirstManager();

    private int thirstLevel;
    private float thirstExhaustion;
    private int waterTimer;
    private float hydrationValue;

    public ThirstManager() {
        this.thirstLevel = 20;
        this.thirstExhaustion = 4.0F;
        this.waterTimer = 20;
        this.hydrationValue = 2F;
    }

    public void write(NbtCompound compound) {
        compound.putInt("ThirstLevel", thirstLevel);
        compound.putFloat("ThirstExhaustion", thirstExhaustion);
        compound.putInt("WaterTimer", waterTimer);
        compound.putFloat("HydrationValue", hydrationValue);
    }

    public void read(NbtCompound compound) {
        /*this.thirstLevel = compound.getInt("ThirstLevel");
        this.thirstExhaustion = compound.getFloat("ThirstExhaustion");
        this.waterTimer = compound.getInt("WaterTimer");
        this.hydrationValue = compound.getFloat("HydrationValue");*/
        //TODO Removing for testing
        this.thirstLevel = 20;
        this.thirstExhaustion = 4.0F;
        this.waterTimer = 20;
        this.hydrationValue = 2F;
    }

    public float getThirstExhaustion() {
        return thirstExhaustion;
    }

    public float getHydrationValue() {
        return hydrationValue;
    }

    public int getThirstLevel() {
        return thirstLevel;
    }

    public int getWaterTimer() {
        return waterTimer;
    }

    public void tick(PlayerEntity player) {
        if(player == null)
            return;
        Difficulty difficulty = player.world.getDifficulty();
        if(difficulty != Difficulty.PEACEFUL) {
            if(thirstExhaustion >= 4.0F) {
                thirstExhaustion -= 2.0F;
                if(hydrationValue > 0.0F) {
                    hydrationValue = Math.max(hydrationValue - 0.5F, 0.0F);
                } else {
                    thirstLevel = Math.max(thirstLevel - 1, 0);
                }
            }

            boolean flag = player.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);
            if(flag && hydrationValue > 0.0F && player.isAlive() && thirstLevel >= 20) {
                waterTimer++;
                if(waterTimer >= 10) {
                    float f = Math.min(hydrationValue, 6.0F);
                    player.heal(f / 12.0F);
                    thirstExhaustion += f;
                    waterTimer = 0;
                }
            } else if(flag && thirstLevel > 0 && player.isAlive()) {
                waterTimer++;
                if(waterTimer >= 80) {
                    player.heal(0.5F);
                    thirstExhaustion += 0.6F;
                    waterTimer = 0;
                }
            } else if(thirstLevel <= 0) {
                waterTimer++;
                if(waterTimer >= 80) {
                    if(player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F || difficulty == Difficulty.NORMAL) {
                        player.damage(DamageSource.MAGIC, 1.0F);
                    }
                    waterTimer = 0;
                }
            } else {
                waterTimer = 0;
            }
        }
    }

}
