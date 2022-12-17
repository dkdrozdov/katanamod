package com.falanero.katanamod.util.ability.diamond;

import com.falanero.katanamod.mixin.LivingEntityInvoker;
import com.falanero.katanamod.util.Nbt;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

import static com.falanero.katanamod.util.Utility.arithmeticProgression;
import static com.falanero.katanamod.util.Utility.toRoman;

public class SkyboundDiamondAbility {
    private static int getLevel(int itemLevel) {
        return arithmeticProgression(2, 2, 6, itemLevel);
    }
    public static void appendTooltip(int itemLevel, List<Text> tooltip){
        int abilityLevel = getLevel(itemLevel);
        if(abilityLevel < 1)
            return;
        tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability.skybound.title", toRoman(abilityLevel)).formatted(Formatting.BOLD));
        tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability.skybound.description.line_1", getAttacksToTrigger(abilityLevel), toRoman(getJumpBoostLevel(abilityLevel))));
        tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability.skybound.description.line_2", getJumpBoostTime(abilityLevel), getThrowHeight(abilityLevel)));
    }

    /**
     * @return duration time in seconds.
     */
    private static float getJumpBoostTime(int abilityLevel){
        return (float) (abilityLevel*0.5);
    }
    private static int getJumpBoostLevel(int abilityLevel){
        return abilityLevel;
    }
    private static float getThrowHeight(int abilityLevel){
        return 4 + (abilityLevel - 1) ;
    }
    private static int getAttacksToTrigger(int abilityLevel){
        return Math.max(7 - abilityLevel + 1, 3);
    }

    private static boolean isTriggered(int abilityLevel, int hitCount){
        int currentHitCountTrigger = getAttacksToTrigger(abilityLevel);

        return hitCount >= currentHitCountTrigger;
    }

    public static void apply(LivingEntity target, LivingEntity player, int abilityLevel){
        if(!player.world.isClient){
            int jumpBoostLevel = getJumpBoostLevel(abilityLevel)-1;
            int jumpBoostTimeTicks = (int)(getJumpBoostTime(abilityLevel) * 20);

            player.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.JUMP_BOOST,
                    jumpBoostTimeTicks,
                    jumpBoostLevel));

            double knockbackResistance;

            knockbackResistance = Math.max(0.0, 1.0 - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
            double throwVelocity = ((1+getThrowHeight(abilityLevel)) * ((LivingEntityInvoker)target).invokeGetJumpVelocity() * Math.min(knockbackResistance, 0.25));
            target.setVelocity(0.0, throwVelocity, 0.0);
            player.setVelocity(0.0, throwVelocity, 0.0);
            player.velocityModified = true;
        }
    }

    public static void tryApply(ItemStack stack, LivingEntity target, LivingEntity attacker, int itemLevel){

        int abilityLevel = getLevel(itemLevel);

        if(abilityLevel < 1)
            return;

        int hitCount = Nbt.getHitCount(stack)+1;
        if (isTriggered(abilityLevel, hitCount)){
            hitCount = 0;
            apply(target, attacker, abilityLevel);
        }
        Nbt.setHitCount(stack, hitCount);
    }
}