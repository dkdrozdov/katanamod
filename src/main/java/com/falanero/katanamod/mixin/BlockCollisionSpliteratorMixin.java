package com.falanero.katanamod.mixin;

import com.falanero.katanamod.callback.BlockCollisionCheckCallback;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockCollisionSpliterator;
import net.minecraft.world.CollisionView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiFunction;

@Mixin(BlockCollisionSpliterator.class)
public abstract class BlockCollisionSpliteratorMixin<T> {

    @Nullable
    @Unique
    private Entity katanamod$entity;

    //    @Override
//    public Entity katanamod$getEntity() {
//        return katanamod$entity;
//    }
//
//    @Override
//    public void katanamod$setEntity(Entity entity) {
//    }
    @Final
    @Shadow
    private BiFunction<BlockPos.Mutable, VoxelShape, T> resultFunction;

    @Final
    @Shadow
    private BlockPos.Mutable pos;

    @Inject(method = "<init>(Lnet/minecraft/world/CollisionView;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;ZLjava/util/function/BiFunction;)V", at = @At(value = "RETURN"))
    private void BlockCollisionSpliterator(CollisionView world, Entity entity, Box box, boolean forEntity, BiFunction<BlockPos.Mutable, VoxelShape, T> resultFunction, CallbackInfo ci) {
        katanamod$entity = entity;
    }

    @ModifyExpressionValue(method = "computeNext", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;intersects(DDDDDD)Z"))
    private boolean findCollisionsForMovementInject(boolean original, @Local VoxelShape voxelShape, @Local BlockState blockState) {
        return original && BlockCollisionCheckCallback.COLLISION_CHECK_CALLBACK_EVENT.invoker().addCollisionCondition(original, katanamod$entity, blockState);
    }

}
