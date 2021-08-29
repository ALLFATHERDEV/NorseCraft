package com.norsecraft.common.entity.dwarf.reputation;

import net.minecraft.entity.player.PlayerEntity;

public interface IReputation {

    void updateReputation(ReputationType type, int value, PlayerEntity target);

    int getPlayerReputation(PlayerEntity player);

}
