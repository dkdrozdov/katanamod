package com.falanero.katanamod.mixin;

import com.falanero.katanamod.callback.OnGetAirStrafingSpeedCallback;
import com.falanero.katanamod.callback.OnKilledByCallback;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(at = @At("HEAD"), method = "onKilledBy")
    private void onKilledByInvoke(@Nullable LivingEntity adversary, CallbackInfo info){
        OnKilledByCallback.EVENT.invoker().notify(adversary);
    }

    @Inject(at = @At(value = "RETURN", ordinal = 1), method = "getMovementSpeed(F)F", cancellable = true)
    private void onGetAirStrafingSpeedInvoke(float slipperiness, CallbackInfoReturnable<Float> cir){
            cir.setReturnValue(OnGetAirStrafingSpeedCallback.ON_GET_AIR_STRAFING_SPEED_CALLBACK_EVENT.invoker().intercept(cir.getReturnValue(), (LivingEntity)(Object)(this)).getValue());
    }
}
