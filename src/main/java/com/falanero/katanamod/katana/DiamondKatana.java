package com.falanero.katanamod.katana;

import com.falanero.katanamod.KatanaMod;
import com.falanero.katanamod.ability.*;
import com.falanero.katanamod.ability.diamond.attack.SkyboundDiamondAbility;
import com.falanero.katanamod.ability.diamond.tick.SpringDiamondAbility;
import com.falanero.katanamod.ability.diamond.tick.SwiftnessDiamondAbility;
import com.falanero.katanamod.item.Items;
import com.falanero.katanamod.item.katana.DiamondKatanaItem;
import com.falanero.katanamod.item.katana.KatanaItem;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class DiamondKatana implements Katana {

    @Override
    public @NotNull Map<Predicate<Item>, ConsumableAbility> getConsumableAbilities() {
        return Map.of(
                (item) -> true, (world, user, hand, level) -> {
                    KatanaMod.LOGGER.info(String.format("[katanamod:OnItemUseCallback]/Event raised: Item Usage (%s used %s)",
                            user.getName().getString(), user.getStackInHand(hand).getItem().getName().getString()));
                    return false;
                }
//                PHANTOM_MEMBRANE, WindbombDiamondAbility.getAbility(),
//                FEATHER, FeatherbladeDiamondAbility.getAbility()
        );
    }

    @Override
    public @NotNull List<AttackAbility> getOnSweepAttackAbilities() {
        return List.of((stack, target, attacker, level) -> {
            KatanaMod.LOGGER.info(String.format("Event raised: Sweeping Attack (%s attacked %s)",
                    attacker.getName().getString(), target.getName().getString()));
        });
    }

    @Override
    public @NotNull List<AttackAbility> getOnCritAttackAbilities() {
        return List.of((stack, target, attacker, level) -> {
            KatanaMod.LOGGER.info(String.format("Event raised: Crit Attack (%s attacked %s)",
                    attacker.getName().getString(), target.getName().getString()));
        });
    }

    @Override
    public @NotNull List<AttackAbility> getOnSprintAttackAbilities() {
        return List.of((stack, target, attacker, level) -> {
            KatanaMod.LOGGER.info(String.format("Event raised: Sprint Attack (%s attacked %s)",
                    attacker.getName().getString(), target.getName().getString()));
        });
    }

    @Override
    public @NotNull List<AttackAbility> getPostAttackAbilities() {
        return List.of((stack, target, attacker, itemLevel) -> {
                    KatanaMod.LOGGER.info(String.format("Event raised: Post Attack (%s attacked %s)",
                            attacker.getName().getString(), target.getName().getString()));
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
        return (KatanaItem) Items.DIAMOND_KATANA;
    }

    @Override
    public Text getDescription() {
        return Text.translatable("katanamod.katana.diamond_katana.description");
    }

    @Override
    public Text getName() {
        return Text.translatable("katanamod.katana.diamond_katana.name");
    }

    @Override
    public Identifier getIconTexture() {
        return Identifier.of(KatanaMod.MOD_ID, "textures/item/diamond_katana.png");
    }

    private static final List<Ability<?>> abilities = List.of(
            Abilities.SKYBOUND_DIAMOND_ABILITY,
            Abilities.SPRING_DIAMOND_ABILITY,
            Abilities.SWIFTNESS_DIAMOND_ABILITY,
            Abilities.FEATHERFALL_DIAMOND_ABILITY,
            Abilities.WEAVE_DIAMOND_ABILITY
    );

    @Override
    public List<Ability<?>> getAbilities() {
        return abilities;
    }
}
