package com.falanero.katanamod.mixin;

import com.falanero.katanamod.callback.BlockCollisionCheckCallback;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {
    @ModifyReturnValue(method = "getCameraCollisionShape(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At(value = "RETURN"))
    private VoxelShape BlockCollisionSpliterator(VoxelShape original, BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (original.isEmpty()) return original;

        var client = MinecraftClient.getInstance();

        if (client != null) {
            var player = client.player;
            boolean collide = BlockCollisionCheckCallback.COLLISION_CHECK_CALLBACK_EVENT.invoker().addCollisionCondition(true, player, state);
            return collide ? original : VoxelShapes.empty();
        } else
            return original;
    }
}
