//package com.falanero.katanamod.mixin;
//
//import com.falanero.katanamod.callback.OnComputeFallDamage;
//import com.falanero.katanamod.callback.OnGetAirStrafingSpeedCallback;
//import com.falanero.katanamod.callback.OnKilledByCallback;
//import com.llamalad7.mixinextras.injector.ModifyReturnValue;
//import net.minecraft.entity.LivingEntity;
//import org.jetbrains.annotations.Nullable;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//@Mixin(LivingEntity.class)
//public class LivingEntityMixin {
//
//
//
////    @Inject(at = @At(value = "RETURN"), method = "computeFallDamage", cancellable = true)
////    private void computeFallDamageInject(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cir){
////        cir.setReturnValue(OnComputeFallDamage.ON_COMPUTE_FALL_DAMAGE_CALLBACK_EVENT.invoker().calculateFallDamage(cir.getReturnValue(), fallDistance, damageMultiplier, (LivingEntity)(Object)(this)).getValue());
////    }
////
//    @Inject(at = @At(value = "RETURN", ordinal = 1), method = "getMovementSpeed(F)F", cancellable = true)
//    private void getAirStrafingSpeedInject(float slipperiness, CallbackInfoReturnable<Float> cir){
//        cir.setReturnValue(OnGetAirStrafingSpeedCallback.ON_GET_AIR_STRAFING_SPEED_CALLBACK_EVENT.invoker().intercept(cir.getReturnValue(), (LivingEntity)(Object)(this)).getValue());
//    }
//}
