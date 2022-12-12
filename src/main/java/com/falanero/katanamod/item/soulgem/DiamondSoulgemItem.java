package com.falanero.katanamod.item.soulgem;

import com.falanero.katanamod.util.Souls;
import com.falanero.katanamod.util.Tooltip;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.List;

public class DiamondSoulgemItem extends SoulGemItem{
    public DiamondSoulgemItem(Settings settings) {
        super(settings);
    }

    public void appendTooltipExtra(Pair<ItemStack, List<Text>> callbackContext){
        Souls.appendTooltipExtraDiamond(callbackContext);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext){
        if(Screen.hasShiftDown()){
            appendTooltipExtra(new Pair(itemStack, tooltip));
        }else{
            super.appendTooltip(itemStack, world, tooltip, tooltipContext);
            tooltip.add(Text.translatable("item.katanamod.tooltip_display_more_info").formatted(Formatting.GRAY));
        }
    }


}
