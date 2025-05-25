package com.falanero.katanamod.katana;

import com.falanero.katanamod.ability.*;
import com.falanero.katanamod.item.katana.DiamondKatanaItem;
import com.falanero.katanamod.item.katana.KatanaItem;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface Katana {
    KatanaItem getItem();
    Text getDescription();
    Text getName();
    List<Ability<?>> getAbilities();

    @NotNull
    abstract Map<Predicate<Item>, ConsumableAbility> getConsumableAbilities();

    @NotNull List<AttackAbility> getOnSweepAttackAbilities();

    @NotNull List<AttackAbility> getOnCritAttackAbilities();

    @NotNull List<AttackAbility> getOnSprintAttackAbilities();

    @NotNull List<AttackAbility> getPostAttackAbilities();

    @NotNull List<TickAbility> getTickAbilities();

    @NotNull List<KillAbility> getKillAbilities();

    Identifier getIconTexture();
}
