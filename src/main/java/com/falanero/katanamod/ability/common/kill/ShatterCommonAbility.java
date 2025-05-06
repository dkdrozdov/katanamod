package com.falanero.katanamod.ability.common.kill;

import com.falanero.katanamod.item.katana.KatanaItem;
import com.falanero.katanamod.ability.OnKatanaBreakAbility;
import com.falanero.katanamod.util.itemStackData.KatanamodItemStackData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;

import java.util.Objects;

public class ShatterCommonAbility implements OnKatanaBreakAbility {
    @Override
    public void apply(LivingEntity entity, ItemStack stack, KatanaItem katanaItem) {
        Item droppedMaterial = katanaItem.getShatterItem();
        ItemStack droppedStack = Objects.requireNonNull(entity.dropItem(droppedMaterial, 2)).getStack();
        ItemStack ironSwordStack = Objects.requireNonNull(entity.dropItem(Items.IRON_SWORD, 1)).getStack();
        ironSwordStack.setDamage((int) (ironSwordStack.getMaxDamage() * 0.7));
        int soulCount = KatanamodItemStackData.getSoulCount(stack);
        if (!droppedStack.hasNbt()) {
            droppedStack.setNbt(new NbtCompound());
        }
        KatanamodItemStackData.setSoulCount(droppedStack, soulCount);
        
    }
}
