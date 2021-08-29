package com.norsecraft.common.entity.dwarf;

import com.norsecraft.common.entity.dwarf.goals.DefendDwarfsTargetGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DwarfWarriorEntity extends DwarfEntity implements Angerable {

    private static final UniformIntProvider RANGED_INTEGER = TimeHelper.betweenSeconds(20, 39);
    private int angerTime;
    private UUID angerTarget;

    public DwarfWarriorEntity(EntityType<? extends PathAwareEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createDwarfWarriorAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 250.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 2.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20.0D);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.add(1, new GoToWalkTargetGoal(this, 1.0D));
        this.goalSelector.add(2, new WanderAroundGoal(this, 1.0));
        this.goalSelector.add(3, new LookAroundGoal(this));

        this.targetSelector.add(0, new DefendDwarfsTargetGoal(this));
        this.targetSelector.add(0, new RevengeGoal(this, PlayerEntity.class));
        this.targetSelector.add(0, new FollowTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(0, new FollowTargetGoal<>(this, MobEntity.class, true));
        this.targetSelector.add(3, new UniversalAngerGoal<>(this, false));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.writeAngerToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if(!world.isClient)
            this.readAngerFromNbt((ServerWorld) this.world, nbt);
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if(!this.world.isClient) {
            this.tickAngerLogic((ServerWorld) this.world, true);
        }
    }

    @Override
    public int getAngerTime() {
        return this.angerTime;
    }

    @Override
    public void setAngerTime(int time) {
        this.angerTime = time;
    }

    @Nullable
    @Override
    public UUID getAngryAt() {
        return this.angerTarget;
    }

    @Override
    public void setAngryAt(@Nullable UUID uuid) {
        this.angerTarget = uuid;
    }

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(RANGED_INTEGER.get(this.random));
    }


    private float getAttackDamage() {
        return (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
    }

    @Override
    public boolean tryAttack(Entity target) {
        this.world.sendEntityStatus(this, (byte) 4);


        float damage = this.getAttackDamage();
        float f = (int) damage > 0 ? damage / 2.0F + (float) this.random.nextInt((int) damage) : damage;
        boolean flag = target.damage(DamageSource.mob(this), f);
        if(flag) {
            target.setVelocity(target.getVelocity().add(0.3D, 0.0D, 0.3D));
            this.applyDamageEffects(this, target);
        }
        return flag;
    }

}
