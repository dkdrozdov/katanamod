package com.falanero.katanamod.util.Ability.Diamond;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

import static com.falanero.katanamod.util.Utility.arithmeticProgression;
import static com.falanero.katanamod.util.Utility.toRoman;

public class SwiftnessDiamondAbility {
    private static int getLevel(int itemLevel) {
        return arithmeticProgression(1, 2, 10, itemLevel);
    }
    public static void appendTooltip(int itemLevel, List<Text> tooltip){
        int abilityLevel = getLevel(itemLevel);
        if(abilityLevel < 1)
            return;

        tooltip.add(Text.translatable("item.katanamod.diamond_katana.swiftness.title", toRoman(abilityLevel)).formatted(Formatting.BOLD));
        tooltip.add(Text.translatable("item.katanamod.diamond_katana.swiftness.description", toRoman(abilityLevel)));
    }

    public static void effectTick(PlayerEntity player, int itemLevel){
        int abilityLevel = getLevel(itemLevel);
        if(abilityLevel < 1)
            return;

        if(!player.world.isClient) {
            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SPEED,
                    1,
                    abilityLevel-1,
                    false,
                    false,
                    true));
        }
    }
}
