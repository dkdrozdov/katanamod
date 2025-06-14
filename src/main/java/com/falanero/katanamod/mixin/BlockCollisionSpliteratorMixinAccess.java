package com.falanero.katanamod.mixin;

import net.minecraft.entity.Entity;

public interface BlockCollisionSpliteratorMixinAccess {
    Entity katanamod$getEntity();

    void katanamod$setEntity( Entity entity);
}
