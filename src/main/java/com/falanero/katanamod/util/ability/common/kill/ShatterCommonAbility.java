package com.falanero.katanamod.util.ability.common.kill;

import com.falanero.katanamod.item.katana.KatanaItem;
import com.falanero.katanamod.util.Nbt;
import com.falanero.katanamod.util.Souls;
import com.falanero.katanamod.util.ability.KillAbility;
import com.falanero.katanamod.util.ability.OnKatanaBreakAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Objects;

import static com.falanero.katanamod.util.Souls.getCurrentLevel;
import static com.falanero.katanamod.util.Souls.getSoulsForLevel;

public class ShatterCommonAbility implements OnKatanaBreakAbility {
    @Override
    public void apply(LivingEntity entity, ItemStack stack, KatanaItem katanaItem) {
        Item droppedMaterial = katanaItem.getShatterItem();
        ItemStack droppedStack = Objects.requireNonNull(entity.dropItem(droppedMaterial, 2)).getStack();
        ItemStack ironSwordStack = Objects.requireNonNull(entity.dropItem(Items.IRON_SWORD, 1)).getStack();
        ironSwordStack.setDamage((int) (ironSwordStack.getMaxDamage() * 0.7));
        int soulCount = Nbt.getSoulCount(stack);
        if (!droppedStack.hasNbt()) {
            droppedStack.setNbt(new NbtCompound());
        }
        Nbt.setSoulCount(droppedStack, soulCount);
        
    }
}
