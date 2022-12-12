package com.falanero.katanamod.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

public interface OnKilledByCallback {
    Event<OnKilledByCallback> EVENT = EventFactory.createArrayBacked(OnKilledByCallback.class,
            (listeners) -> (LivingEntity adversary) ->{
                for (OnKilledByCallback listener : listeners){
                    listener.notify(adversary);
                }
            });

    void notify(LivingEntity adversary);
}
