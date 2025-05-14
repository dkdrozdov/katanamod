package com.falanero.katanamod.mixin;

import com.falanero.katanamod.KatanaMod;
import com.falanero.katanamod.callback.OnAttackCallback;
import com.falanero.katanamod.callback.OnGetAirStrafingSpeedCallback;
import com.falanero.katanamod.callback.PlayerEntityTickCallback;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @ModifyReturnValue(
            method = "getOffGroundSpeed()F", at = @At(value = "RETURN", ordinal = 1))
    private float modifyOffGroundSpeed(float original) {
        return OnGetAirStrafingSpeedCallback.ON_GET_AIR_STRAFING_SPEED_CALLBACK_EVENT.invoker().intercept(original, (PlayerEntity) (Object) this).getRight();
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void onPlayerEntityTick(CallbackInfo info) {
        PlayerEntityTickCallback.EVENT.invoker().notify((PlayerEntity) (Object) this);
    }

    //    @Inject(method = "attack",
//            at = @At(value = "HEAD"),
//            locals = LocalCapture.CAPTURE_FAILHARD)
//    private void OnAttackInject(Entity target, CallbackInfo ci) {
////        KatanaMod.LOGGER.info("It's an attack!!! !!!");
////        if(((PlayerEntity)(Object)this).world.isClient)
////            KatanaMod.LOGGER.info("client!");
////        else
////            KatanaMod.LOGGER.info("server!");
//    }
    @Inject(method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;spawnSweepAttackParticles()V"))
    private void OnSweepingAttackInject(Entity target, CallbackInfo ci) {
        if (target.getWorld() instanceof ServerWorld serverWorld) {
            OnAttackCallback.ON_SWEEPING_ATTACK_CALLBACK_EVENT.invoker().notify(target, ((PlayerEntity) (Object) this));
        }
    }

    @Inject(method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addCritParticles(Lnet/minecraft/entity/Entity;)V"))
    private void OnCritAttackInject(Entity target, CallbackInfo ci) {
        if (target.getWorld() instanceof ServerWorld serverWorld) {
            OnAttackCallback.ON_CRIT_ATTACK_CALLBACK_EVENT.invoker().notify(target, ((PlayerEntity) (Object) this));
        }
    }

    @Inject(method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V", ordinal = 0))
    private void OnSprintAttackInject(Entity target, CallbackInfo ci) {
        if (target.getWorld() instanceof ServerWorld serverWorld) {
            OnAttackCallback.ON_SPRINT_ATTACK_CALLBACK_EVENT.invoker().notify(target, ((PlayerEntity) (Object) this));
        }
    }

}
