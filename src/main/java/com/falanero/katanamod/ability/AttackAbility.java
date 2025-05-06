package com.falanero.katanamod.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

@FunctionalInterface
public interface AttackAbility {
    void apply(ItemStack stack, LivingEntity target, LivingEntity attacker, int itemLevel);
}
