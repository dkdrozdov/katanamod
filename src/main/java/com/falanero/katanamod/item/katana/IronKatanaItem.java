package com.falanero.katanamod.item.katana;

import com.falanero.katanamod.callback.ToolBreakCallback;
import com.falanero.katanamod.util.ability.ConsumableAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import java.util.Collections;
import java.util.Map;

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

    @Override
    protected Map<Item, ConsumableAbility> getConsumableAbilities() {
        return Collections.emptyMap();
    }

}
