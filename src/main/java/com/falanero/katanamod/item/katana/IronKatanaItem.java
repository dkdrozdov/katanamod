package com.falanero.katanamod.item.katana;

import com.falanero.katanamod.callback.ToolBreakCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class IronKatanaItem extends KatanaItem{

    public IronKatanaItem(int attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, settings);
        ToolBreakCallback.EVENT.register((entity) -> {
            onKatanaBreak(entity, Items.IRON_INGOT, false);
        });
    }

    @Override
    protected void onKatanaBreak(LivingEntity entity, Item droppedMaterial, boolean preserveNbt)
    {
        ItemStack stack = entity.getStackInHand(Hand.MAIN_HAND);
        if(stack.getItem() instanceof IronKatanaItem){
            super.onKatanaBreak(entity, droppedMaterial, preserveNbt);
        }
    }

}
