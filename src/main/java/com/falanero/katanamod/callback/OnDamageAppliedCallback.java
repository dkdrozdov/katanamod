package com.falanero.katanamod.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public interface OnDamageAppliedCallback {
    /**
     * Fires at each successful sweeping attack when sweeping particles spawn.
     */
    Event<OnDamageAppliedCallback> ON_DAMAGE_APPLIED_EVENT = EventFactory.createArrayBacked(OnDamageAppliedCallback.class,
            (listeners) -> (ServerWorld world, DamageSource source, float amount, LivingEntity entity) -> {
                for (OnDamageAppliedCallback listener : listeners) {
                    listener.notify(world, source, amount, entity);
                }
            });


    void notify(ServerWorld world, DamageSource source, float amount, LivingEntity entity);
}
