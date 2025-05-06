package com.falanero.katanamod.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;

public interface OnComputeFallDamage {
    /**
     * Fires in the end of fall damage calculation method. Listeners can override the result.
     */
    Event<OnComputeFallDamage> ON_COMPUTE_FALL_DAMAGE_CALLBACK_EVENT = EventFactory.createArrayBacked(OnComputeFallDamage.class,
            (listeners) -> (int fallDamage, float fallDistance, float damageMultiplier, LivingEntity entity) ->{
                for (OnComputeFallDamage listener : listeners){
                    TypedActionResult<Integer> current = listener.calculateFallDamage(fallDamage, fallDistance, damageMultiplier, entity);
                    if(current.getResult() != ActionResult.PASS)
                        return current;

                }
                return new TypedActionResult<>(ActionResult.PASS, fallDamage);
            });
    TypedActionResult<Integer> calculateFallDamage(int fallDamage, float fallDistance, float damageMultiplier, LivingEntity entity);
}
