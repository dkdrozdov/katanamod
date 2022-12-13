package com.falanero.katanamod.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public interface OnAttackCallback {
    Event<OnAttackCallback> ON_SWEEPING_ATTACK_CALLBACK_EVENT = EventFactory.createArrayBacked(OnAttackCallback.class,
            (listeners) -> (Entity target, PlayerEntity player) ->{
                for (OnAttackCallback listener : listeners){
                    listener.notify(target, player);
                }
            });
    Event<OnAttackCallback> ON_CRIT_ATTACK_CALLBACK_EVENT = EventFactory.createArrayBacked(OnAttackCallback.class,
            (listeners) -> (Entity target, PlayerEntity player) ->{
                for (OnAttackCallback listener : listeners){
                    listener.notify(target, player);
                }
            });
    Event<OnAttackCallback> ON_SPRINT_ATTACK_CALLBACK_EVENT = EventFactory.createArrayBacked(OnAttackCallback.class,
            (listeners) -> (Entity target, PlayerEntity player) ->{
                for (OnAttackCallback listener : listeners){
                    listener.notify(target, player);
                }
            });
    void notify(Entity adversary, PlayerEntity player);
}
