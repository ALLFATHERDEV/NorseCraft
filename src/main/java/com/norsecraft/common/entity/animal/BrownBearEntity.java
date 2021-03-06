package com.norsecraft.common.entity.animal;

import com.norsecraft.common.entity.dwarf.AbstractDwarfEntity;
import com.norsecraft.common.network.NetworkHelper;
import com.norsecraft.common.network.s2c.SendAttackingEntityS2C;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class BrownBearEntity extends HostileEntity implements IAnimatable, ClientAttackAnimationProvider {

    public static final String ANIMATION_WALK = "animation.brown_bear.walk";
    public static final String ANIMATION_IDLE = "animation.brown_bear.idle";
    public static final String ANIMATION_ATTACK = "animation.brown_bear.attack";
    public static final String ANIMATION_RUN = "animation.brown_bear.run";

    private final AnimationFactory factory = new AnimationFactory(this);

    @Environment(EnvType.CLIENT)
    private LivingEntity clientAttacker;

    public BrownBearEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createBrownBearAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 40)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.55F)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.7);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new FixedMeleeAttackGoal(this, 0.55, false));
        this.goalSelector.add(1, new WanderAroundFarGoal(this, 0.55));
        this.goalSelector.add(2, new LookAroundGoal(this));

        this.targetSelector.add(0, new FixedFollowTargetGoal<>(this, PlayerEntity.class, false));
        this.targetSelector.add(3, new FixedFollowTargetGoal<>(this, VillagerEntity.class, false));
        this.targetSelector.add(3, new FixedFollowTargetGoal<>(this, AnimalEntity.class, false));
        this.targetSelector.add(3, new FixedFollowTargetGoal<>(this, AbstractDwarfEntity.class, false));
    }

    protected double getSquaredMaxAttackDistance(LivingEntity entity) {
        return this.getWidth() * 2.0F * getWidth() * 2.0F + entity.getWidth();
    }

    @Override
    public void tick() {
        super.tick();
    }

    private <E extends IAnimatable> PlayState running(AnimationEvent<E> event) {
        LivingEntity onClientAttacker = this.getCurrentAttackerOnClient();
        if (onClientAttacker == null) {
            return PlayState.STOP;
        }

        if (event.isMoving() && !playAttack) {
            double d = this.squaredDistanceTo(onClientAttacker.getX(), onClientAttacker.getY(), onClientAttacker.getZ());
            if (d > getSquaredMaxAttackDistance(onClientAttacker) + 2) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIMATION_RUN));
            }
        }
        return PlayState.CONTINUE;

    }

    private <E extends IAnimatable> PlayState attackAnimation(AnimationEvent<E> event) {
        if (playAttack) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIMATION_ATTACK));
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving() && this.getCurrentAttackerOnClient() == null && !playAttack) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIMATION_WALK));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIMATION_IDLE, true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "movement", 0, this::predicate));
        animationData.addAnimationController(new AnimationController<>(this, "running", 0, this::running));
        animationData.addAnimationController(new AnimationController<>(this, "attack", 0, this::attackAnimation));
    }

    private boolean playAttack = false;

    public void setPlayAttack(boolean playAttack) {
        this.playAttack = playAttack;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public LivingEntity getCurrentAttackerOnClient() {
        return this.clientAttacker;
    }

    @Override
    public void setCurrentAttackerOnClient(LivingEntity entity) {
        this.clientAttacker = entity;
    }


    /**
     * This is a fixed version of the {@link MeleeAttackGoal} from mc
     * In this version I broadcast a packet to every client player with the information of the current attacking entity
     *
     */
    private static class FixedMeleeAttackGoal extends MeleeAttackGoal {

        public FixedMeleeAttackGoal(BrownBearEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
        }

        @Override
        public void tick() {
            super.tick();
            if(mob.world.isClient) {
                ((BrownBearEntity) mob).setPlayAttack(true);
            }
        }

        @Override
        public void stop() {
            super.stop();
            //If the world is not the client world, then the world is the server world and i can broadcast a packet to the client with the attacker entity id
            if (!mob.world.isClient) {
                NetworkHelper.broadcastToClient((ServerWorld) this.mob.world, SendAttackingEntityS2C.ID, new SendAttackingEntityS2C(this.mob.getId(),
                        -1, SendAttackingEntityS2C.NONE));
            }
            if(mob.world.isClient) {
                ((BrownBearEntity) mob).setPlayAttack(false);
            }
        }
    }

    /**
     * Same as {@link FixedMeleeAttackGoal} only for the {@link FollowTargetGoal}
     * @param <T>
     */
    private static class FixedFollowTargetGoal<T extends LivingEntity> extends FollowTargetGoal<T> {

        public FixedFollowTargetGoal(BrownBearEntity mob, Class<T> targetClass, boolean checkVisibility) {
            super(mob, targetClass, checkVisibility);
        }

        @Override
        public void start() {
            super.start();
            if (!mob.world.isClient) {
                NetworkHelper.broadcastToClient((ServerWorld) this.mob.world, SendAttackingEntityS2C.ID, new SendAttackingEntityS2C(this.mob.getId(),
                        this.targetEntity.getId()));
            }
        }
    }

}
