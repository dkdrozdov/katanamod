package com.falanero.katanamod.item.katana;

import com.falanero.katanamod.callback.ToolBreakCallback;
import com.falanero.katanamod.util.ability.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class IronKatanaItem extends KatanaItem {

    public IronKatanaItem(int attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, settings);
    }

    @Override
    protected @NotNull Map<Item, ConsumableAbility> getConsumableAbilities() {
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
    protected @NotNull List<OnKatanaBreakAbility> getOnKatanaBreakAbilities() {
        return Collections.emptyList();
    }


    @Override
    public @NotNull Item getShatterItem() {
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
