package com.falanero.katanamod.mixin;

import com.falanero.katanamod.callback.OnAttackCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(PlayerEntity.class)
public class OnAttackMixin {

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
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;spawnSweepAttackParticles()V"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void OnSweepingAttackInject(Entity target, CallbackInfo ci) {
        if(!((PlayerEntity)(Object)this).world.isClient){
            OnAttackCallback.ON_SWEEPING_ATTACK_CALLBACK_EVENT.invoker().notify(target, ((PlayerEntity)(Object)this));
        }
    }

    @Inject(method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V", ordinal = 2),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void OnCritAttackInject(Entity target, CallbackInfo ci) {
        if(!((PlayerEntity)(Object)this).world.isClient){
            OnAttackCallback.ON_CRIT_ATTACK_CALLBACK_EVENT.invoker().notify(target, ((PlayerEntity)(Object)this));
        }
    }
    @Inject(method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void OnSprintAttackInject(Entity target, CallbackInfo ci) {
        if(!((PlayerEntity)(Object)this).world.isClient){
            OnAttackCallback.ON_SPRINT_ATTACK_CALLBACK_EVENT.invoker().notify(target, ((PlayerEntity)(Object)this));
        }
    }
}
