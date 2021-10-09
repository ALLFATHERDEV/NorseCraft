package com.norsecraft.common.entity.dwarf.reputation;

import net.minecraft.entity.player.PlayerEntity;

/**
 * If a dwarf entity has a reputation it should implement th is interface
 */
public interface IReputation {

    void updateReputation(ReputationType type, int value, PlayerEntity target);

    int getPlayerReputation(PlayerEntity player);

}
