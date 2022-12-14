package com.falanero.katanamod.mixin;

import com.falanero.katanamod.callback.OnKilledByCallback;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(at = @At("HEAD"), method = "onKilledBy")
    private void onKilledByInvoke(@Nullable LivingEntity adversary, CallbackInfo info){
        OnKilledByCallback.EVENT.invoker().notify(adversary);
    }
}
