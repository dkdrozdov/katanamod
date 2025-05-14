package com.falanero.katanamod.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public interface OnGetAirStrafingSpeedCallback {
    /**
     * Fires on movement speed calculation and intercepts the air strafing speed getter.
     */
    Event<OnGetAirStrafingSpeedCallback> ON_GET_AIR_STRAFING_SPEED_CALLBACK_EVENT = EventFactory.createArrayBacked(OnGetAirStrafingSpeedCallback.class,
            (listeners) -> (float original, PlayerEntity entity) ->{
                for (OnGetAirStrafingSpeedCallback listener : listeners){
                    Pair<Boolean, Float> current = listener.intercept(original, entity);
                    if(current.getLeft())
                        return new ImmutablePair<>(true, current.getRight());
                }
                return new ImmutablePair<>(false, original);
            });

    /**
     * @return pair of boolean that indicates if the new value should replace the old one and float - the new value.
     */
    Pair<Boolean, Float> intercept(float original, PlayerEntity entity);
}