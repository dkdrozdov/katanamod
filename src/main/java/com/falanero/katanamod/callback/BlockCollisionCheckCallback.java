package com.falanero.katanamod.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;

public interface BlockCollisionCheckCallback {

    /**
     * Fires each time a collision check of an entity with a block is conducted.
     * Delegate returns additional condition for block to collide with the entity.
     */
    Event<BlockCollisionCheckCallback> COLLISION_CHECK_CALLBACK_EVENT = EventFactory.createArrayBacked(BlockCollisionCheckCallback.class,
            (listeners) -> (boolean original, Entity entity, BlockState blockState) -> {
                boolean additionalCondition = true;
                for (BlockCollisionCheckCallback listener : listeners) {
                    boolean current = listener.addCollisionCondition(original, entity, blockState);

                    additionalCondition = additionalCondition && current;
                }
                return original && additionalCondition;
            });

    boolean addCollisionCondition(boolean original, Entity entity, BlockState blockState);
}
