package com.falanero.katanamod.util;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;

public class Utility {
    static public Hand getHandByEquipmentSlot(EquipmentSlot slot){
        if(slot.getType() != EquipmentSlot.Type.HAND)
            return null;
        return slot == EquipmentSlot.MAINHAND ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }


    /**
     *
     * @param startingPoint the iteration where the progression returns 1.
     * @param incrementPeriod the number of iterations needed to increment progression's returned value. Should be greater than 0.
     * @param maxValue the progression's returned value upper bound.
     * @return progression's value in current iteration.
     */
    static public int arithmeticProgression(int startingPoint, int incrementPeriod, int maxValue, int iteration){
        return MathHelper.clamp((iteration>=startingPoint ? 1 : 0) * (1 + (iteration-startingPoint)/incrementPeriod), 0, maxValue);
    }
    static public String toRoman(int number){
        return switch (number) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            case 11 -> "XI";
            case 12 -> "XII";
            case 13 -> "XIII";
            case 14 -> "XIV";
            case 15 -> "XV";
            case 16 -> "XVI";
            case 17 -> "XVII";
            case 18 -> "XVIII";
            case 19 -> "XIX";
            case 20 -> "XX";
            case 21 -> "XXI";
            case 22 -> "XXII";
            case 23 -> "XXIII";
            case 24 -> "XXIV";
            case 25 -> "XXV";
            default -> "";
        };
    }
}
