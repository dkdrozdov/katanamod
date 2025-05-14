package com.falanero.katanamod.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public interface OnItemUseCallback {
    /**
     * Fires on successful item usage attempt if the initial action result is not consumed.
     */
    Event<UseItemCallback> ON_ITEM_USE_CALLBACK = UseItemCallback.EVENT;

}
