package com.falanero.katanamod.mixin;

import com.falanero.katanamod.callback.OnKilledByCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class OnAttackedByMixin {
    @Inject(at = @At("TAIL"), method = "damage")
    private void OnAttackedByInvoke(DamageSource source, float amount, CallbackInfo info){
        Entity entity = source.getAttacker();
        if(entity == null)
            return;

        if(entity instanceof PlayerEntity) {
            if (source instanceof EntityDamageSource && ((EntityDamageSource) source).isThorns()) {
                return;
            }

        }


//        OnAttackedByCallback.EVENT.invoker().notify();
    }
}
