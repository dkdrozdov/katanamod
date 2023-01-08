package com.falanero.katanamod.util.ability;

import net.minecraft.entity.player.PlayerEntity;

public interface TickAbility {
    void apply(PlayerEntity player, int itemLevel);
}
