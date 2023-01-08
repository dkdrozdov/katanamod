package com.falanero.katanamod.item.soulgem;

import com.falanero.katanamod.util.Nbt;
import com.falanero.katanamod.util.ability.diamond.FeatherfallDiamondAbility;
import com.falanero.katanamod.util.ability.diamond.attack.SkyboundDiamondAbility;
import com.falanero.katanamod.util.ability.diamond.consumable.FeatherbladeDiamondAbility;
import com.falanero.katanamod.util.ability.diamond.consumable.WindbombDiamondAbility;
import com.falanero.katanamod.util.ability.diamond.tick.SpringDiamondAbility;
import com.falanero.katanamod.util.ability.diamond.tick.SwiftnessDiamondAbility;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

import static com.falanero.katanamod.util.Souls.getCurrentLevel;

public class DiamondSoulgemItem extends SoulGemItem{
    public DiamondSoulgemItem(Settings settings) {
        super(settings);
    }

    public static void appendExtra(ItemStack stack, List<Text> tooltip){
        int level = getCurrentLevel(Nbt.getSoulCount(stack));

        tooltip.add(Text.translatable("item.katanamod.diamond_katana.element_description"));
        SkyboundDiamondAbility.appendTooltip(level, tooltip);
        SwiftnessDiamondAbility.appendTooltip(level, tooltip);
        SpringDiamondAbility.appendTooltip(level, tooltip);
        FeatherfallDiamondAbility.appendTooltip(level, tooltip);
        WindbombDiamondAbility.appendTooltip(level, tooltip);
        FeatherbladeDiamondAbility.appendTooltip(level, tooltip);
    }

    public void appendTooltipExtra(ItemStack stack, List<Text> tooltip){
        appendExtra(stack, tooltip);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext){
        if(Screen.hasShiftDown()){
            appendTooltipExtra(itemStack, tooltip);
        }else{
            super.appendTooltip(itemStack, world, tooltip, tooltipContext);
            tooltip.add(Text.translatable("item.katanamod.tooltip_display_more_info").formatted(Formatting.GRAY));
        }
    }
}
