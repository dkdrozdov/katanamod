package com.falanero.katanamod.item.soulgem;

import com.falanero.katanamod.util.Souls;
import com.falanero.katanamod.util.itemStackData.KatanamodItemStackData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

public class SoulGemItem extends Item {

    public SoulGemItem(Settings settings) {
        super(settings);
    }
//
//    @Override
//    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipType tooltipContext) {
//        tooltip.add(Text.translatable("item.katanamod.soulgem_item.tooltip_line_1"));
//        Souls.appendTooltipSoulCount(itemStack, tooltip);
//    }
//
//    @Override
//    public Text getName(ItemStack stack) {
//        return Text.translatable(this.getTranslationKey(stack), Souls.getCurrentLevel(KatanamodItemStackData.getSoulCount(stack)));
//    }
}
