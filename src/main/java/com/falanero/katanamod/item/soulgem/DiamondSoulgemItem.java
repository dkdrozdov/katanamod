package com.falanero.katanamod.item.soulgem;

import com.falanero.katanamod.ability.diamond.tick.SpringDiamondAbility;
import com.falanero.katanamod.ability.diamond.tick.SwiftnessDiamondAbility;
import com.falanero.katanamod.util.itemStackData.KatanamodItemStackData;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

import static com.falanero.katanamod.util.Souls.getCurrentLevel;

public class DiamondSoulgemItem extends SoulGemItem {
    public DiamondSoulgemItem(Settings settings) {
        super(settings);
    }


    public static void appendExtra(ItemStack stack, Consumer<Text> tooltip) {
        int level = getCurrentLevel(KatanamodItemStackData.getSoulCount(stack));

        tooltip.accept(Text.translatable("item.katanamod.diamond_katana.element_description"));
//        SkyboundDiamondAbility.appendTooltip(level, tooltip);
        SwiftnessDiamondAbility.appendTooltip(level, tooltip);
        SpringDiamondAbility.appendTooltip(level, tooltip);
//        FeatherfallDiamondAbility.appendTooltip(level, tooltip);
//        WindbombDiamondAbility.appendTooltip(level, tooltip);
//        FeatherbladeDiamondAbility.appendTooltip(level, tooltip);
    }

    public void appendTooltipExtra(ItemStack stack, Consumer<Text> tooltip) {
        appendExtra(stack, tooltip);
    }

//    @Override
//    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipType tooltipContext){
//        if(Screen.hasShiftDown()){
//            appendTooltipExtra(itemStack, tooltip);
//        }else{
//            super.appendTooltip(itemStack, world, tooltip, tooltipContext);
//            tooltip.add(Text.translatable("item.katanamod.tooltip_display_more_info").formatted(Formatting.GRAY));
//        }
//    }
}
