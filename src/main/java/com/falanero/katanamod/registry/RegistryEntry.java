package com.falanero.katanamod.registry;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public record RegistryEntry<V, T extends V>(Registry<V> registry, String id, T instance) {
    public void register(String MOD_ID){
        Registry.register(registry, new Identifier(MOD_ID, id), instance);
    }
}
