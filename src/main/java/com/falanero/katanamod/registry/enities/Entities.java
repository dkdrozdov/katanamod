package com.falanero.katanamod.registry.enities;

import com.falanero.katanamod.registry.IDs;
import net.minecraft.util.registry.Registry;

public class Entities {
    public static void register() {
        Registry.register(Registry.ENTITY_TYPE, IDs.FEATHERBLADE_ENTITY, Instances.FEATHERBLADE_ENTITY);
    }
}
