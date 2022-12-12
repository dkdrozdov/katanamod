package com.falanero.katanamod.util;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;

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
        int soulCount = Nbt.getSoulCount(stack);
        int level = getCurrentLevel(soulCount);
        int soulsCurrent = soulCount - getSoulsForLevel(level);
        int soulsNeeded = getSoulsNeeded(level+1);

        tooltip.add(Text.translatable("item.katanamod.tooltip_souls", soulsCurrent, soulsNeeded));
    }

    public static void appendTooltipExtraDiamond(Pair<ItemStack, List<Text>> callbackContext){
        ItemStack stack = callbackContext.getLeft();
        List<Text> tooltip = callbackContext.getRight();
        int level = getCurrentLevel(Nbt.getSoulCount(stack));

        tooltip.add(Text.translatable("item.katanamod.diamond_katana.element_description"));
        switch (level){
            case 1:
                tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability_1.level_1.name").formatted(Formatting.BOLD));
                tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability_1.level_1.description"));
                break;
            case 2:
                tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability_1.level_2.name").formatted(Formatting.BOLD));
                tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability_1.level_2.description"));
                tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability_2.level_1.name").formatted(Formatting.BOLD));
                tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability_2.level_1.description_line_1"));
                tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability_2.level_1.description_line_2"));

            default:
                break;
        }
    }

    static public void appendTooltipLevel(ItemStack stack, List<Text> tooltip){
        int level = getCurrentLevel(Nbt.getSoulCount(stack));

        tooltip.add(Text.translatable("item.katanamod.tooltip_level", level));
    }
}
