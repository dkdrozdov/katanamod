package com.falanero.katanamod.mixin;

import com.falanero.katanamod.callback.OnItemUseCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(at = @At(value = "RETURN", ordinal = 2), method = "use", cancellable = true)
    private void onUseInvoke(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir){
        ActionResult actionResult = OnItemUseCallback.ON_ITEM_USE_CALLBACK.invoker().use(world, user, hand);
        TypedActionResult<ItemStack> stackResult;
        ItemStack itemStack = user.getStackInHand(hand);

        if(actionResult == ActionResult.CONSUME)
            stackResult = TypedActionResult.consume(itemStack);
        else if(actionResult == ActionResult.PASS)
            stackResult = TypedActionResult.pass(user.getStackInHand(hand));
        else
            stackResult = TypedActionResult.fail(itemStack);

        cir.setReturnValue(stackResult);
    }
}
