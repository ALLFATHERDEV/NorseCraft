package com.norsecraft.common.entity.dwarf.goals;

import com.norsecraft.common.entity.dwarf.AbstractDwarfEntity;
import com.norsecraft.common.entity.dwarf.DwarfEntity;
import com.norsecraft.common.entity.dwarf.DwarfWarriorEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.DistancePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Box;

import java.util.EnumSet;
import java.util.List;

/**
 * Goals are the AI tasks for the entities.
 * This goals is for the warrior dwarfs.
 *
 * If a player attacks a dwarf all warrior dwarfs will be attack the player
 */
public class DefendDwarfsTargetGoal extends TrackTargetGoal {

    private final DwarfWarriorEntity dwarfWarrior;
    private LivingEntity villageAgressorTarget;

    public DefendDwarfsTargetGoal(DwarfWarriorEntity dwarfWarrior) {
        super(dwarfWarrior, false);
        this.dwarfWarrior = dwarfWarrior;
        this.setControls(EnumSet.of(Control.TARGET));
    }

    @Override
    public boolean canStart() {
        Box axisalignedbb = this.dwarfWarrior.getBoundingBox().expand(10.0D, 8.0D, 10.0D);
        List<AbstractDwarfEntity> nearbyDwarfs = this.dwarfWarrior.world.getEntitiesByClass(AbstractDwarfEntity.class, axisalignedbb, EntityPredicates.maxDistance(96, 96, 96, 96));
        List<PlayerEntity> nearbyPlayers = this.dwarfWarrior.world.getPlayers(TargetPredicate.DEFAULT, dwarfWarrior, axisalignedbb);

        for (AbstractDwarfEntity dw : nearbyDwarfs) {
            for (PlayerEntity player : nearbyPlayers) {
                int rep = dw.getPlayerReputation(player);
                if (rep <= -25) {
                    this.villageAgressorTarget = player;
                }
            }
        }
        if (this.villageAgressorTarget == null)
            return false;
        else
            return !(this.villageAgressorTarget instanceof PlayerEntity) || !this.villageAgressorTarget.isSpectator() && !((PlayerEntity)this.villageAgressorTarget).isCreative();
    }

    @Override
    public boolean shouldContinue() {
       return true;
    }

    @Override
    public void start() {
        this.dwarfWarrior.setAngryAt(this.villageAgressorTarget.getUuid());
        super.start();
    }
}
