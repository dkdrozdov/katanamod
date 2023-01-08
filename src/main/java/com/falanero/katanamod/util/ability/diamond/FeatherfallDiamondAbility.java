package com.falanero.katanamod.util.ability.diamond;

import com.falanero.katanamod.util.ability.TickAbility;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;

import java.util.List;

import static com.falanero.katanamod.util.Utility.arithmeticProgression;
import static com.falanero.katanamod.util.Utility.toRoman;

public class FeatherfallDiamondAbility {
    private static int getLevel(int itemLevel) {
        return arithmeticProgression(1, 2, 10, itemLevel);
    }

    public static void appendTooltip(int itemLevel, List<Text> tooltip) {
        int abilityLevel = getLevel(itemLevel);
        if (abilityLevel < 1)
            return;

        tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability.featherfall.title", toRoman(abilityLevel)).formatted(Formatting.BOLD));
        tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability.featherfall.description", ((int)((1-getReduction(abilityLevel))*100))));
    }

    private static float getReduction(int abilityLevel){
        return 1f - (4 + 7 * (float)abilityLevel)/100f;
    }
    public static TypedActionResult<Integer> onComputeFallDamage(int fallDamage, float fallDistance, float damageMultiplier, LivingEntity entity, int itemLevel) {
        int abilityLevel = getLevel(itemLevel);
        if (abilityLevel < 1)
            return new TypedActionResult<>(ActionResult.FAIL, fallDamage);
        if(entity.isSneaking()){
            int rawDamage = MathHelper.ceil((fallDistance - 3.0f) * damageMultiplier);
            float reductionFactor = getReduction(abilityLevel);
            return new TypedActionResult<>(ActionResult.SUCCESS, Math.min((int) Math.floor(rawDamage * reductionFactor), fallDamage));
        }
        return new TypedActionResult<>(ActionResult.FAIL, fallDamage);
    }

}
