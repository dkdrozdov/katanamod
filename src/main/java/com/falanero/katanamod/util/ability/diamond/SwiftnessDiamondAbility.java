package com.falanero.katanamod.util.ability.diamond;

import com.falanero.katanamod.item.katana.DiamondKatanaItem;
import com.falanero.katanamod.util.Nbt;
import com.falanero.katanamod.util.Souls;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.TypedActionResult;

import java.util.List;

import static com.falanero.katanamod.util.Utility.arithmeticProgression;
import static com.falanero.katanamod.util.Utility.toRoman;

public class SwiftnessDiamondAbility {
    private static int getLevel(int itemLevel) {
        return arithmeticProgression(1, 2, 10, itemLevel);
    }

    public static void appendTooltip(int itemLevel, List<Text> tooltip) {
        int abilityLevel = getLevel(itemLevel);
        if (abilityLevel < 1)
            return;

        tooltip.add(Text.translatable("item.katanamod.diamond_katana.swiftness.title", toRoman(abilityLevel)).formatted(Formatting.BOLD));
        tooltip.add(Text.translatable("item.katanamod.diamond_katana.swiftness.description", toRoman(abilityLevel)));
    }

    public static TypedActionResult<Float> onGetAirStrafingSpeed(float airStrafingSpeed, LivingEntity entity) {
        if (entity instanceof PlayerEntity player && player.getMainHandStack().getItem() instanceof DiamondKatanaItem){
        int abilityLevel = getLevel(Souls.getCurrentLevel(Nbt.getSoulCount(player.getMainHandStack())));
            if(abilityLevel<1)
                return new TypedActionResult<>(ActionResult.FAIL, airStrafingSpeed);
            return new TypedActionResult<>(ActionResult.SUCCESS,  Math.max (player.getMovementSpeed()/8.328f, airStrafingSpeed));
        }
        return new TypedActionResult<>(ActionResult.FAIL, airStrafingSpeed);
    }

    public static void effectTick(PlayerEntity player, int itemLevel) {
        int abilityLevel = getLevel(itemLevel);
        if (abilityLevel < 1)
            return;

        if (!player.world.isClient) {
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
                if (player.isOnGround()) {
                    effectLevel = (abilityLevel-1)/2;
                    effectTickTime = 10;
                } else {
                    effectLevel = (abilityLevel) * 2 - 1;
                    effectTickTime = 2;
                }

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
