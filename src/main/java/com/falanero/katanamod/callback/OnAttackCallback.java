package com.falanero.katanamod.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface OnAttackCallback {
    /**
     * Fires after each katana hit.
     */
    Event<OnAttackCallback> POST_HIT_CALLBACK_EVENT = EventFactory.createArrayBacked(OnAttackCallback.class,
            (listeners) -> (Entity target, PlayerEntity attacker) -> {
                for (OnAttackCallback listener : listeners) {
                    listener.notify(target, attacker);
                }
            });

    /**
     * Fires at each successful sweeping attack when sweeping particles spawn.
     */
    Event<OnAttackCallback> ON_SWEEPING_ATTACK_CALLBACK_EVENT = EventFactory.createArrayBacked(OnAttackCallback.class,
            (listeners) -> (Entity target, PlayerEntity attacker) -> {
                for (OnAttackCallback listener : listeners) {
                    listener.notify(target, attacker);
                }
            });

    /**
     * Fires at each successful critical attack when sound is played.
     */
    Event<OnAttackCallback> ON_CRIT_ATTACK_CALLBACK_EVENT = EventFactory.createArrayBacked(OnAttackCallback.class,
            (listeners) -> (Entity target, PlayerEntity attacker) -> {
                for (OnAttackCallback listener : listeners) {
                    listener.notify(target, attacker);
                }
            });

    /**
     * Fires at each successful sprint attack when knockback sound is played.
     */
    Event<OnAttackCallback> ON_SPRINT_ATTACK_CALLBACK_EVENT = EventFactory.createArrayBacked(OnAttackCallback.class,
            (listeners) -> (Entity target, PlayerEntity attacker) -> {
                for (OnAttackCallback listener : listeners) {
                    listener.notify(target, attacker);
                }
            });

    void notify(Entity target, PlayerEntity attacker);
}
