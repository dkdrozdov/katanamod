package com.falanero.katanamod.registry.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolMaterials;

public class Settings {
    public static final FabricItemSettings IRON_KATANA = new FabricItemSettings().maxDamage(ToolMaterials.IRON.getDurability()/3).group(ItemGroup.COMBAT);
    public static final FabricItemSettings DIAMOND_KATANA = new FabricItemSettings().maxDamage(ToolMaterials.IRON.getDurability()/3).group(ItemGroup.COMBAT);
    public static final FabricItemSettings DIAMOND_SOULGEM = new FabricItemSettings().group(ItemGroup.MISC).maxCount(1);
}
