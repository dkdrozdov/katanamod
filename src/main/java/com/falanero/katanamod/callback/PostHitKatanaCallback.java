package com.falanero.katanamod.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface PostHitKatanaCallback {
    /**
     * Fires after each katana hit.
     */


    void notify(ItemStack stack, LivingEntity target, LivingEntity attacker);

}
