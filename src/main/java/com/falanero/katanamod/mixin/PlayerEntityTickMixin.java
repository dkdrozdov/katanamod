package com.falanero.katanamod.mixin;

import com.falanero.katanamod.callback.PlayerEntityTickCallback;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityTickMixin {
    @Inject(at = @At("TAIL"), method = "tick")
    private void onPlayerEntityTick(CallbackInfo info){
        PlayerEntityTickCallback.EVENT.invoker().notify((PlayerEntity)(Object)this);
    }
}
