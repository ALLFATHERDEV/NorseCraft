package com.norsecraft.common.entity.animal;

import net.minecraft.entity.LivingEntity;

/**
 * If a entity has a attack animation it should be implement this interface.
 * Because the current attacker is set on the client sided and will NOT be synced to the client.
 * That is a problem, because we need the current attacker on the client side for the attack animation.
 * But with this interface it will be synced.
 * See {@link BrownBearEntity} for more information
 *
 */
public interface ClientAttackAnimationProvider {

    /**
     * @return the current attacker on the client side
     */
    LivingEntity getCurrentAttackerOnClient();

    /**
     * Set the current attacker on the client side
     *
     * @param entity the current attacker on the clients ide
     */
    void setCurrentAttackerOnClient(LivingEntity entity);

}
