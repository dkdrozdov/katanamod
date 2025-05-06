package com.falanero.katanamod.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public interface PlayerEntityTickCallback {
    /**
     * Fires after each player tick.
     */
    Event<PlayerEntityTickCallback> EVENT = EventFactory.createArrayBacked(PlayerEntityTickCallback.class,
            (listeners) -> (PlayerEntity player) ->{
                for (PlayerEntityTickCallback listener : listeners){
                    listener.notify(player);
                }
            });

    void notify(PlayerEntity player);
}
