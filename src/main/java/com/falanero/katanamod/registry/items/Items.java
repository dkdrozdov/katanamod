package com.falanero.katanamod.registry.items;

import com.falanero.katanamod.registry.IDs;
import net.minecraft.util.registry.Registry;

public class Items {
    public static void register() {
        Registry.register(Registry.ITEM, IDs.IRON_KATANA, Instances.IRON_KATANA);
        Registry.register(Registry.ITEM, IDs.DIAMOND_KATANA, Instances.DIAMOND_KATANA);
        Registry.register(Registry.ITEM, IDs.DIAMOND_SOULGEM, Instances.DIAMOND_SOULGEM);
    }
}
