package com.falanero.katanamod.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

public interface OnKilledByCallback {
    /**
     * Fires at the start of onKilledBy.
     */
    Event<OnKilledByCallback> EVENT = EventFactory.createArrayBacked(OnKilledByCallback.class,
            (listeners) -> (LivingEntity killer) ->{
                for (OnKilledByCallback listener : listeners){
                    listener.notify(killer);
                }
            });

    void notify(LivingEntity killer);
}
