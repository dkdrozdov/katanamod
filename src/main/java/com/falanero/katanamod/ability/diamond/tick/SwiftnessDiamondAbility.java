package com.falanero.katanamod.ability.diamond.tick;

import com.falanero.katanamod.ability.TickAbility;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Consumer;

import static com.falanero.katanamod.util.Utility.arithmeticProgression;
import static com.falanero.katanamod.util.Utility.toRoman;

public class SwiftnessDiamondAbility {
    private static int getLevel(int itemLevel) {
        return arithmeticProgression(1, 2, 10, itemLevel);
    }

    public static void appendTooltip(int itemLevel, Consumer<Text> tooltip) {
        int abilityLevel = getLevel(itemLevel);
        if (abilityLevel < 1)
            return;

        tooltip.accept(Text.translatable("item.katanamod.diamond_katana.swiftness.title", toRoman(abilityLevel)).formatted(Formatting.BOLD));
        tooltip.accept(Text.translatable("item.katanamod.diamond_katana.swiftness.description", toRoman(abilityLevel)));
    }

    public static Pair<Boolean, Float> onGetAirStrafingSpeed(float original, PlayerEntity player, int itemLevel) {
        int abilityLevel = getLevel(itemLevel);
        if (abilityLevel < 1)
            return new ImmutablePair<>(false, original);
        var speed = player.getMovementSpeed();
        return new ImmutablePair<>(true, player.isSprinting() ? speed / 3.846f : speed / 5.0f);
    }

    public static TickAbility getAbility() {
        return SwiftnessDiamondAbility::apply;
    }

    public static void apply(PlayerEntity player, int itemLevel) {
        int abilityLevel = getLevel(itemLevel);
        if (abilityLevel < 1)
            return;

        if (player.getWorld() instanceof ServerWorld serverWorld) {
            {
//                int effectLevel = player.isOnGround() ? abilityLevel - 1 : abilityLevel * 4 - 1;
                int effectLevel = abilityLevel - 1;

                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.SPEED,
                        2,
                        effectLevel,
                        false,
                        false,
                        true));
            }
            // Demo-section
            if (player.isSneaking()) {
                int effectLevel;
                int effectTickTime;
                effectLevel = (abilityLevel - 1) / 2;
                effectTickTime = 10;

                player.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.JUMP_BOOST,
                        effectTickTime,
                        effectLevel,
                        false,
                        false,
                        true));
            }
        }
    }
}
