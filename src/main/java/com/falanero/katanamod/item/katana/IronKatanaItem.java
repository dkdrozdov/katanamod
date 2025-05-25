package com.falanero.katanamod.item.katana;

import com.falanero.katanamod.katana.Katana;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class IronKatanaItem extends KatanaItem {

    public IronKatanaItem(int attackDamage, float attackSpeed, Item.Settings settings, Katana katana) {
        super(attackDamage, attackSpeed, settings, katana);
    }

    @Override
    protected boolean hasSeizeAbility() {
        return false;
    }

    @Override
    public void appendTooltipExtra(ItemStack itemStack, Consumer<Text> tooltip) {

    }

    @Override
    protected void appendInlaidKatanaDescription(Consumer<Text> tooltip) {

    }

}
