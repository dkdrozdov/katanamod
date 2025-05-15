package com.falanero.katanamod.item.katana;

import com.falanero.katanamod.ability.AttackAbility;
import com.falanero.katanamod.ability.ConsumableAbility;
import com.falanero.katanamod.ability.KillAbility;
import com.falanero.katanamod.ability.TickAbility;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class IronKatanaItem extends KatanaItem {

    public IronKatanaItem(int attackDamage, float attackSpeed, Item.Settings settings) {
        super(attackDamage, attackSpeed, settings);
    }

    @Override
    protected @NotNull Map<Predicate<Item>, ConsumableAbility> getConsumableAbilities() {
        return Collections.emptyMap();
    }

    @Override
    protected @NotNull List<AttackAbility> getOnSweepAttackAbilities() {
        return Collections.emptyList();
    }

    @Override
    protected @NotNull List<AttackAbility> getOnCritAttackAbilities() {
        return Collections.emptyList();
    }

    @Override
    protected @NotNull List<AttackAbility> getOnSprintAttackAbilities() {
        return Collections.emptyList();
    }

    @Override
    protected @NotNull List<AttackAbility> getPostAttackAbilities() {
        return Collections.emptyList();
    }

    @Override
    protected @NotNull List<TickAbility> getTickAbilities() {
        return Collections.emptyList();
    }

    @Override
    protected @NotNull List<KillAbility> getKillAbilities() {
        return Collections.emptyList();
    }

    @Override
    protected boolean hasSeizeAbility() {
        return false;
    }

//    @Override
//    public void appendTooltipExtra(ItemStack itemStack, List<Text> tooltip) {
//
//    }
//
//    @Override
//    protected void appendInlaidKatanaDescription(List<Text> tooltip) {
//
//    }

}
