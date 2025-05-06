package com.falanero.katanamod.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface ConsumableAbility {
    boolean apply(World world, PlayerEntity user, Hand hand, int itemLevel);
}
