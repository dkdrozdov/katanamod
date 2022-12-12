package com.falanero.katanamod.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

public interface ToolBreakCallback {

    Event<ToolBreakCallback> EVENT = EventFactory.createArrayBacked(ToolBreakCallback.class,
        (listeners) -> (entity) ->{
        for (ToolBreakCallback listener : listeners){
            listener.notify(entity);
        }
    });

    void notify(LivingEntity entity);
}
