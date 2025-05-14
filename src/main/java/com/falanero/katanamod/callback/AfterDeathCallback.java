package com.falanero.katanamod.callback;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.Event;

public interface AfterDeathCallback {
    /**
     * Fires when an entity kills another entity via attacking.
     */
    Event<ServerLivingEntityEvents.AfterDeath> EVENT = ServerLivingEntityEvents.AFTER_DEATH;
}
