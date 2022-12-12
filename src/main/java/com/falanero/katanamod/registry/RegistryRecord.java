package com.falanero.katanamod.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.util.registry.Registry;

public record RegistryRecord<T>(Registry registry, String itemId, FabricItemSettings settings, T instance) {
    T getInstance(){
        return instance;
    }
}
