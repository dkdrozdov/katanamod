package com.falanero.katanamod.ability;

import net.minecraft.entity.player.PlayerEntity;

public interface TickAbility {
    void apply(PlayerEntity player, int itemLevel);
}
