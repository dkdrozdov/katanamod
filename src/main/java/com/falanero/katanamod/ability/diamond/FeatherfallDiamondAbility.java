package com.falanero.katanamod.ability.diamond;

import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Consumer;

import static com.falanero.katanamod.util.Utility.arithmeticProgression;
import static com.falanero.katanamod.util.Utility.toRoman;

public class FeatherfallDiamondAbility {
    private static int getLevel(int itemLevel) {
        return arithmeticProgression(1, 2, 10, itemLevel);
    }

    public static void appendTooltip(int itemLevel, Consumer<Text> tooltip) {
        int abilityLevel = getLevel(itemLevel);
        if (abilityLevel < 1)
            return;

        tooltip.accept(Text.translatable("item.katanamod.diamond_katana.ability.featherfall.title", toRoman(abilityLevel)).formatted(Formatting.BOLD));
        tooltip.accept(Text.translatable("item.katanamod.diamond_katana.ability.featherfall.description", ((int) ((1 - getReduction(abilityLevel)) * 100))));
    }

    private static float getReduction(int abilityLevel) {
        return 1f - (4 + 7 * (float) abilityLevel) / 100f;
    }

    public static Pair<Boolean, Integer> onComputeFallDamage(int fallDamage, double fallDistance, float damageMultiplier, LivingEntity entity, int itemLevel) {
        int abilityLevel = getLevel(itemLevel);
        if (abilityLevel < 1)
            return new ImmutablePair<>(false, fallDamage);
        if (entity.isSneaking()) {
//            int rawDamage = MathHelper.ceil((fallDistance - 3.0f) * damageMultiplier);
//            float reductionFactor = getReduction(abilityLevel);
//            return new ImmutablePair<>(true, Math.min((int) Math.floor(rawDamage * reductionFactor), fallDamage));
            return new ImmutablePair<>(true, 0);
        }
        return new ImmutablePair<>(false, fallDamage);
    }

}
