package com.falanero.katanamod.mixin;

import com.falanero.katanamod.callback.OnBlockCollisionCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "onBlockCollision(Lnet/minecraft/block/BlockState;)V", at = @At(value = "RETURN"))
    private void findCollisionsForMovementInject(BlockState state, CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        OnBlockCollisionCallback.ON_BLOCK_COLLISION_CALLBACK_EVENT.invoker().notify(entity, state);
    }

}
