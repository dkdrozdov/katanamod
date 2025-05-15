package com.falanero.katanamod.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public interface OnComputeFallDamage {
    /**
     * Fires in the end of fall damage calculation method. Listeners can override the result.
     */
    Event<OnComputeFallDamage> ON_COMPUTE_FALL_DAMAGE_CALLBACK_EVENT = EventFactory.createArrayBacked(OnComputeFallDamage.class,
            (listeners) -> (int fallDamage, double fallDistance, float damageMultiplier, LivingEntity entity) ->{
                for (OnComputeFallDamage listener : listeners){
                    Pair<Boolean, Integer> current = listener.calculateFallDamage(fallDamage, fallDistance, damageMultiplier, entity);
                    if(current.getLeft())
                        return current;
                }
                return new ImmutablePair<>(false, fallDamage);
            });
    Pair<Boolean, Integer> calculateFallDamage(int fallDamage, double fallDistance, float damageMultiplier, LivingEntity entity);
}
