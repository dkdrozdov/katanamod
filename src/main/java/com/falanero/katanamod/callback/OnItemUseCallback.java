package com.falanero.katanamod.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface OnItemUseCallback {
    /**
     * Fires on successful item usage attempt if the initial action result is not consumed.
     */
    Event<OnItemUseCallback> ON_ITEM_USE_CALLBACK = EventFactory.createArrayBacked(OnItemUseCallback.class,
            (listeners) -> (World world, PlayerEntity user, Hand hand) ->{
                for (OnItemUseCallback listener : listeners) {
                    ActionResult result = listener.use(world, user, hand);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult use(World world, PlayerEntity user, Hand hand);
}
