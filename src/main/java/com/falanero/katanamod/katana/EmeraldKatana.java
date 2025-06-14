package com.falanero.katanamod.katana;

import com.falanero.katanamod.KatanaMod;
import com.falanero.katanamod.ability.*;
import com.falanero.katanamod.item.Items;
import com.falanero.katanamod.item.katana.KatanaItem;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class EmeraldKatana implements Katana {

    @Override
    public @NotNull Map<Predicate<Item>, ConsumableAbility> getConsumableAbilities() {
        return Map.of(
                (item) -> true, (world, user, hand, level) -> {
                    return false;
                }

        );
    }

    @Override
    public @NotNull List<AttackAbility> getOnSweepAttackAbilities() {
        return List.of((stack, target, attacker, level) -> {
        });
    }

    @Override
    public @NotNull List<AttackAbility> getOnCritAttackAbilities() {
        return List.of((stack, target, attacker, level) -> {
        });
    }

    @Override
    public @NotNull List<AttackAbility> getOnSprintAttackAbilities() {
        return List.of((stack, target, attacker, level) -> {
        });
    }

    @Override
    public @NotNull List<AttackAbility> getPostAttackAbilities() {
        return List.of((stack, target, attacker, itemLevel) -> {
                }
        );
    }

    @Override
    public @NotNull List<TickAbility> getTickAbilities() {
        return List.of(
        );
    }

    @Override
    public @NotNull List<KillAbility> getKillAbilities() {
        return Collections.emptyList();
    }

    @Override
    public KatanaItem getItem() {
        return (KatanaItem) Items.EMERALD_KATANA;
    }

    @Override
    public Text getDescription() {
        return Text.translatable("katanamod.katana.emerald_katana.description");
    }

    @Override
    public Text getName() {
        return Text.translatable("katanamod.katana.emerald_katana.name");
    }

    @Override
    public Identifier getIconTexture() {
        return Identifier.of(KatanaMod.MOD_ID, "textures/item/emerald_katana.png");
    }

    private static final List<Ability<?>> abilities = List.of(
            Abilities.BUSHWALK_EMERALD_ABILITY,
            Abilities.MELD_EMERALD_ABILITY
    );

    @Override
    public List<Ability<?>> getAbilities() {
        return abilities;
    }
}
