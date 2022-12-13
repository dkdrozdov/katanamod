package com.falanero.katanamod.registry;

import net.minecraft.util.registry.Registry;

public enum RegistryRecords {
    IRON_KATANA(new RegistryRecord(Registry.ITEM, ItemIDs.IRON_KATANA, Settings.IRON_KATANA, Instances.IRON_KATANA)),
    DIAMOND_KATANA(new RegistryRecord(Registry.ITEM, ItemIDs.DIAMOND_KATANA, Settings.DIAMOND_KATANA, Instances.DIAMOND_KATANA)),
    DIAMOND_SOULGEM(new RegistryRecord(Registry.ITEM, ItemIDs.DIAMOND_SOULGEM, Settings.DIAMOND_SOULGEM, Instances.DIAMOND_SOULGEM));
    RegistryRecords(RegistryRecord rec){ this.record = rec; }

    final public RegistryRecord record;
}
