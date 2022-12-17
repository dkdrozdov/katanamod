package com.falanero.katanamod.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public interface OnGetAirStrafingSpeedCallback {
    Event<OnGetAirStrafingSpeedCallback> ON_GET_AIR_STRAFING_SPEED_CALLBACK_EVENT = EventFactory.createArrayBacked(OnGetAirStrafingSpeedCallback.class,
            (listeners) -> (float airStrafingSpeed, LivingEntity entity) ->{
                for (OnGetAirStrafingSpeedCallback listener : listeners){
                    TypedActionResult<Float> current = listener.intercept(airStrafingSpeed, entity);
                    if(current.getResult() != ActionResult.PASS)
                        return current;

                }
                return new TypedActionResult<>(ActionResult.PASS, airStrafingSpeed);
            });
    TypedActionResult<Float> intercept(float airStrafingSpeed, LivingEntity entity);
}