package com.norsecraft.common.entity.animal;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class WildBoarEntity extends HostileEntity implements IAnimatable {


    private static final String ANIMATION_IDLE = "animation.boar.idle";
    private static final String ANIMATION_WALK = "animation.boar.walk";
    private static final double SPEED = 0.4;

    private AnimationFactory factory = new AnimationFactory(this);

    public WildBoarEntity(EntityType<? extends HostileEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static DefaultAttributeContainer.Builder createWildBoarAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 60.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, SPEED, false));
        this.goalSelector.add(1, new WanderAroundFarGoal(this, SPEED));
        this.goalSelector.add(2, new LookAroundGoal(this));

        this.targetSelector.add(0, new FollowTargetGoal<>(this, PlayerEntity.class, false));
        this.targetSelector.add(3, new FollowTargetGoal<>(this, VillagerEntity.class, false));
        this.targetSelector.add(3, new FollowTargetGoal<>(this, AnimalEntity.class, false));

    }


    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIMATION_WALK));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(ANIMATION_IDLE, true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
