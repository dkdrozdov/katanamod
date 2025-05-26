package com.falanero.katanamod.item.katana;

import com.falanero.katanamod.katana.Katana;
import net.minecraft.item.Item;

public class IronKatanaItem extends KatanaItem {

    public IronKatanaItem(int attackDamage, float attackSpeed, Item.Settings settings, Katana katana) {
        super(attackDamage, attackSpeed, settings, katana);
    }

    @Override
    protected boolean hasSeizeAbility() {
        return false;
    }

}
