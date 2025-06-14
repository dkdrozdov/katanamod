package com.falanero.katanamod.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public interface OnBlockCollisionCallback {
    Event<OnBlockCollisionCallback> ON_BLOCK_COLLISION_CALLBACK_EVENT = EventFactory.createArrayBacked(OnBlockCollisionCallback.class,
            (listeners) -> (Entity target, BlockState state) -> {
                for (OnBlockCollisionCallback listener : listeners) {
                    listener.notify(target, state);
                }
            });

    void notify(Entity entity, BlockState state);
}
