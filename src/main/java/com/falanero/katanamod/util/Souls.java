package com.falanero.katanamod.util;

import com.falanero.katanamod.util.itemStackData.KatanamodItemStackData;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class Souls {
    public static final int SOUL_INCREASE_PER_LEVEL = 100;

    /**
     * @return amount of souls needed to get to {@code level} from previous level.
     * @param level next level.
     * **/
    static public int getSoulsNeeded(int level){
        return (level - 1) * SOUL_INCREASE_PER_LEVEL;
    }

    /**
     * @return level based on {@code soulCount}.
     * @param soulCount summary amount of souls collected over all levels.
     * **/
    static public int getCurrentLevel(int soulCount){
        return (int)(1f/2f + Math.sqrt(1f/4f + 2f*(float)soulCount/(float)SOUL_INCREASE_PER_LEVEL));
    }

    /**
     * @return summary amount of souls needed to get to {@code level}.
     * **/
    static public int getSoulsForLevel(int level){
        return level*SOUL_INCREASE_PER_LEVEL*(level-1)/2;
    }

    static public void appendTooltipSoulCount(ItemStack stack, List<Text> tooltip){
        int soulCount = KatanamodItemStackData.getSoulCount(stack);
        int level = getCurrentLevel(soulCount);
        int soulsCurrent = soulCount - getSoulsForLevel(level);
        int soulsNeeded = getSoulsNeeded(level+1);

        tooltip.add(Text.translatable("item.katanamod.tooltip_souls", soulsCurrent, soulsNeeded));
    }

    static public void appendTooltipLevel(ItemStack stack, List<Text> tooltip){
        int level = getCurrentLevel(KatanamodItemStackData.getSoulCount(stack));

        tooltip.add(Text.translatable("item.katanamod.tooltip_level", level));
    }
}
