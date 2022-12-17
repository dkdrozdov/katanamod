package com.falanero.katanamod.registry;

import java.util.ArrayList;

public class RegistryEntries extends ArrayList<RegistryEntry<?, ?>> {
    public RegistryEntries() {
        super();
        this.add(new RegistryEntry<>(MinecraftRegistries.IRON_KATANA, IDs.IRON_KATANA, Instances.IRON_KATANA));
        this.add(new RegistryEntry<>(MinecraftRegistries.DIAMOND_KATANA, IDs.DIAMOND_KATANA, Instances.DIAMOND_KATANA));
        this.add(new RegistryEntry<>(MinecraftRegistries.DIAMOND_SOULGEM, IDs.DIAMOND_SOULGEM, Instances.DIAMOND_SOULGEM));
        this.add(new RegistryEntry<>(MinecraftRegistries.FEATHERBLADE, IDs.FEATHERBLADE, Instances.FEATHERBLADE));
    }
}
