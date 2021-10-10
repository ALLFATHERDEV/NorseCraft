package com.norsecraft.common.thirst;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;

public class ThirstManager {
    /**
     * Rate that water level is decremented
     */
    private static final int WATER_DECREMENT = 1;
    /**
     * The minimum level at which player will still receive healing.
     */
    private static final int WATER_HEAL_LEVEL = 18;
    /**
     * Rate to heal from a high enough water level.
     */
    private static final int WATER_HEAL_RATE = 1;
    /**
     * Maximum value the water level can achieve
     */
    private static final int WATER_MAX = 20;
    /**
     * Amount to trigger hydration decrement
     */
    private static final float DEHYDRATION_TRIGGER = 4.0F;
    /**
     * Max amount of dehydration possible.
     */
    private static final float DEHYDRATION_MAX = 40.0F;
    /**
     * Rate to increment hydration levels.
     */
    private static final float HYDRATION_INCREMENT = 1.0F;
    /**
     * Rate to heal with hydration levels (hydration/point of healing)
     */
    private static final float HYDRATION_HEAL_RATE = 6.0F;
    /**
     * Tick rate to trigger hydration healing.
     */
    private static final int HYDRATE_HEAL_TICK_TRIGGER = 10;
    /**
     * Tick rate to trigger water healing.
     */
    private static final int WATER_HEAL_TICK_TRIGGER = 80;
    /**
     * Minimum health level to apply damage due to dehydration on EASY difficulty
     */
    private static final float EASY_MIN_HEALTH = 10.0F;
    /**
     * Minimum health level to apply damage due to dehydration
     */
    private static final float NORMAL_MIN_HEALTH = 1.0F;
    private static final float EMPTY = 0.0F;
    /**
     * NBT compound property names.
     */
    private static final String WATER_LEVEL = "waterLevel";
    private static final String HYDRATION_LEVEL = "hydrationLevel";
    private static final String DEHYDRATION_LEVEL = "dehydrationLevel";
    private static final String THIRST_TICK_TIMER = "thirstTickTimer";

    private int waterLevel = 20;
    private float hydrationLevel = 5.0F;
    private float dehydrationLevel;
    private int thirstTickTimer;

    /**
     * drink an amount of water.
     * @param water amount of water to drink
     * @param hydrationModifier modifier for hydration levels
     */
    public void add(int water, float hydrationModifier) {
        waterLevel = Math.min(water + waterLevel, WATER_MAX);
        // what is the magic number?
        hydrationLevel = Math.min(hydrationLevel + (float)water * hydrationModifier * 2.0F, waterLevel);
    }

    /**
     * Tick Update function, updates levels and health if healing or damage should be done.
     * @param player player to apply this to.
     */
    public void update(PlayerEntity player) {
        var difficulty = player.world.getDifficulty();
        // has enough dehydration built up to take action?
        if (dehydrationLevel > DEHYDRATION_TRIGGER) {
            dehydrationLevel -= DEHYDRATION_TRIGGER;
            // decrement hydration level if none, decrement water level
            if (hydrationLevel > EMPTY) {
                hydrationLevel = Math.max(hydrationLevel - HYDRATION_INCREMENT, EMPTY);
            } else if (difficulty != Difficulty.PEACEFUL) {
                waterLevel = Math.max(waterLevel - WATER_DECREMENT, (int)EMPTY); 
            }
        }

        boolean canRegenerate = player.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);
        // check if healing/damaging should be done
        if (canRegenerate && hydrationLevel > EMPTY && player.canFoodHeal() && waterLevel >= WATER_MAX) {
            ++thirstTickTimer;
            if (thirstTickTimer >= HYDRATE_HEAL_TICK_TRIGGER) {
                float hydrationRate = Math.min(hydrationLevel, HYDRATION_HEAL_RATE);
                player.heal(hydrationRate / HYDRATION_HEAL_RATE);
                addDehydration(hydrationRate);
                thirstTickTimer = 0;
            }
        } else if (canRegenerate && waterLevel >= WATER_HEAL_LEVEL && player.canFoodHeal()) {
            ++thirstTickTimer;
            if (thirstTickTimer >= WATER_HEAL_TICK_TRIGGER) {
                player.heal(WATER_HEAL_RATE);
                addDehydration(HYDRATION_HEAL_RATE);
                thirstTickTimer = 0;
            }
        } else if (waterLevel <= 0) {
            ++thirstTickTimer;
            if (thirstTickTimer >= WATER_HEAL_TICK_TRIGGER) {
                var health = player.getHealth();
                if (health > EASY_MIN_HEALTH || difficulty == Difficulty.HARD || (health > NORMAL_MIN_HEALTH && difficulty == Difficulty.NORMAL)) {
                    player.damage(DamageSource.STARVE, WATER_HEAL_RATE);
                }
                thirstTickTimer = 0;
            }
        } else {
            // not healing/damaging, reset timer
            this.thirstTickTimer = 0;
        }
    }

    /**
     * Add dehydration levels.
     * @param dehydration dehydration amount to add to current.
     */
    public void addDehydration(float dehydration) {
        this.dehydrationLevel = Math.min(dehydration + this.dehydrationLevel, DEHYDRATION_MAX);
    }

    public int getWaterLevel() {
        return waterLevel;
    }
    
    /**
     * Write data to nbt for storage.
     * @param nbt nbt compound to add to.
     */
    public void write(NbtCompound nbt) {
        nbt.putInt(WATER_LEVEL, waterLevel); 
        nbt.putInt(THIRST_TICK_TIMER, thirstTickTimer);
        nbt.putFloat(HYDRATION_LEVEL, hydrationLevel);
        nbt.putFloat(DEHYDRATION_LEVEL, dehydrationLevel);
    }

    /**
     * Read data from nbt into this object.
     * @param nbt nbt compound to read from.
     */
    public void read(NbtCompound nbt) {
        if (nbt.contains(WATER_LEVEL, 99)) {
            waterLevel = nbt.getInt(WATER_LEVEL);
            thirstTickTimer = nbt.getInt(THIRST_TICK_TIMER);
            hydrationLevel = nbt.getFloat(HYDRATION_LEVEL);
            dehydrationLevel = nbt.getFloat(DEHYDRATION_LEVEL);
        }
    }
}
