package com.falanero.katanamod.mixin;

import com.falanero.katanamod.KatanaMod;
import com.falanero.katanamod.callback.AfterDeathCallback;
import com.falanero.katanamod.callback.OnComputeFallDamage;
import com.falanero.katanamod.callback.OnDamageAppliedCallback;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setHealth(F)V"))
    private void onDamageAppliedInject(ServerWorld world, DamageSource source, float amount, CallbackInfo ci) {
        OnDamageAppliedCallback.ON_DAMAGE_APPLIED_EVENT.invoker().notify(world, source, amount, (LivingEntity) (Object) (this));
    }


    @ModifyReturnValue(at = @At(value = "RETURN", ordinal = 1), method = "computeFallDamage")
    private int computeFallDamageInject(int original, double fallDistance, float damagePerDistance) {
        return OnComputeFallDamage.ON_COMPUTE_FALL_DAMAGE_CALLBACK_EVENT.invoker().calculateFallDamage(original, fallDistance, damagePerDistance, (LivingEntity) (Object) (this)).getRight();
    }

}
