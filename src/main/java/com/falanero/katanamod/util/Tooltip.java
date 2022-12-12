package com.falanero.katanamod.util;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class Tooltip {
    static public void appendTooltipDisplayMoreInfo(ItemStack stack, List<Text> tooltip, Consumer<Pair<ItemStack, List<Text>>> displayMoreInfoCallback) {
        if(Screen.hasShiftDown()){
            displayMoreInfoCallback.accept(new Pair(stack, tooltip));
        }else{
            tooltip.add(Text.translatable("item.katanamod.tooltip_display_more_info").formatted(Formatting.GRAY));
        }
    }
}
