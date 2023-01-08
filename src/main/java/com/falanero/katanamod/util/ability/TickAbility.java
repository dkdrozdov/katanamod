package com.falanero.katanamod.util.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface TickAbility {
    void effectTick(PlayerEntity player, int itemLevel);
}
