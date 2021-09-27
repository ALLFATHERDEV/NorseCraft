package com.norsecraft.common.entity.animal;

import net.minecraft.entity.LivingEntity;

public interface ClientAttackAnimationProvider {

    LivingEntity getCurrentAttackerOnClient();

    void setCurrentAttackerOnClient(LivingEntity entity);

}
