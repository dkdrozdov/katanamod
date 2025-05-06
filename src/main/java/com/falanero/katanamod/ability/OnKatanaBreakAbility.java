package com.falanero.katanamod.ability;

import com.falanero.katanamod.item.katana.KatanaItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface OnKatanaBreakAbility {
    void apply(LivingEntity owner, ItemStack stack, KatanaItem katanaItem);
}
