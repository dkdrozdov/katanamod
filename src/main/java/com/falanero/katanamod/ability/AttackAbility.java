package com.falanero.katanamod.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface AttackAbility {

    void apply(ItemStack stack, LivingEntity target, LivingEntity attacker, int itemLevel);
}
