//package com.falanero.katanamod.mixin;
//
//import com.falanero.katanamod.KatanaMod;
//import com.falanero.katanamod.callback.OnAttackCallback;
//import com.falanero.katanamod.callback.PlayerEntityTickCallback;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.player.PlayerEntity;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
//
//@Mixin(PlayerEntity.class)
//public class PlayerEntityMixin {
//    @Inject(at = @At("TAIL"), method = "tick")
//    private void onPlayerEntityTick(CallbackInfo info){
//        PlayerEntityTickCallback.EVENT.invoker().notify((PlayerEntity)(Object)this);
//    }
//
//    //    @Inject(method = "attack",
////            at = @At(value = "HEAD"),
////            locals = LocalCapture.CAPTURE_FAILHARD)
////    private void OnAttackInject(Entity target, CallbackInfo ci) {
//////        KatanaMod.LOGGER.info("It's an attack!!! !!!");
//////        if(((PlayerEntity)(Object)this).world.isClient)
//////            KatanaMod.LOGGER.info("client!");
//////        else
//////            KatanaMod.LOGGER.info("server!");
////    }
//    @Inject(method = "attack",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;spawnSweepAttackParticles()V"),
//            locals = LocalCapture.CAPTURE_FAILHARD)
//    private void OnSweepingAttackInject(Entity target, CallbackInfo ci) {
//        if(!((PlayerEntity)(Object)this).world.isClient){
//            OnAttackCallback.ON_SWEEPING_ATTACK_CALLBACK_EVENT.invoker().notify(target, ((PlayerEntity)(Object)this));
//        }
//    }
//
//    @Inject(method = "attack",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V", ordinal = 2),
//            locals = LocalCapture.CAPTURE_FAILHARD)
//    private void OnCritAttackInject(Entity target, CallbackInfo ci) {
//        if(!((PlayerEntity)(Object)this).world.isClient){
//            OnAttackCallback.ON_CRIT_ATTACK_CALLBACK_EVENT.invoker().notify(target, ((PlayerEntity)(Object)this));
//        }
//    }
//    @Inject(method = "attack",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V", ordinal = 0),
//            locals = LocalCapture.CAPTURE_FAILHARD)
//    private void OnSprintAttackInject(Entity target, CallbackInfo ci) {
//        if(!((PlayerEntity)(Object)this).world.isClient){
//            OnAttackCallback.ON_SPRINT_ATTACK_CALLBACK_EVENT.invoker().notify(target, ((PlayerEntity)(Object)this));
//        }
//    }
//
//}
