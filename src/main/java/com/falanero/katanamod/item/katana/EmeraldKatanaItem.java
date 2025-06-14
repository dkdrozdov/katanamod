package com.falanero.katanamod.item.katana;

import com.falanero.katanamod.katana.Katana;

public class EmeraldKatanaItem extends KatanaItem {

    public EmeraldKatanaItem(int attackDamage, float attackSpeed, Settings settings, Katana katana) {
        super(attackDamage, attackSpeed, settings, katana);
    }

    @Override
    protected boolean hasSeizeAbility() {
        return true;
    }

}
