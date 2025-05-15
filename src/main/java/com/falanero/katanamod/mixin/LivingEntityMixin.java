package com.falanero.katanamod.mixin;

import com.falanero.katanamod.callback.OnComputeFallDamage;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @ModifyReturnValue(at = @At(value = "RETURN", ordinal = 1), method = "computeFallDamage")
    private int computeFallDamageInject(int original, double fallDistance, float damagePerDistance) {
        return OnComputeFallDamage.ON_COMPUTE_FALL_DAMAGE_CALLBACK_EVENT.invoker().calculateFallDamage(original, fallDistance, damagePerDistance, (LivingEntity) (Object) (this)).getRight();
    }
}
