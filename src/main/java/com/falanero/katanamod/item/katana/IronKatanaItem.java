package com.falanero.katanamod.item.katana;

import com.falanero.katanamod.callback.ToolBreakCallback;
import com.falanero.katanamod.util.ability.AttackAbility;
import com.falanero.katanamod.util.ability.ConsumableAbility;
import com.falanero.katanamod.util.ability.KillAbility;
import com.falanero.katanamod.util.ability.TickAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class IronKatanaItem extends KatanaItem {

    public IronKatanaItem(int attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, settings);
    }

    @Override
    protected Map<Item, ConsumableAbility> getConsumableAbilities() {
        return Collections.emptyMap();
    }

    @Override
    protected AttackAbility getOnSweepAttackAbility() {
        return null;
    }

    @Override
    protected AttackAbility getOnCritAttackAbility() {
        return null;
    }

    @Override
    protected AttackAbility getOnSprintAttackAbility() {
        return null;
    }

    @Override
    protected AttackAbility getPostAttackAbility() {
        return null;
    }

    @Override
    protected TickAbility getTickAbility() {
        return null;
    }

    @Override
    protected KillAbility getKillAbility() {
        return null;
    }

    @Override
    public Item getShatterItem() {
        return Items.IRON_INGOT;
    }

    @Override
    protected boolean hasSeizeAbility() {
        return false;
    }

    @Override
    public void appendTooltipExtra(ItemStack itemStack, List<Text> tooltip) {

    }

    @Override
    protected void appendInlaidKatanaDescription(List<Text> tooltip) {

    }

}
