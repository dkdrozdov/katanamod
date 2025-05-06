package com.falanero.katanamod.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface KillAbility {
    void apply(LivingEntity killer, ItemStack stack);
}
