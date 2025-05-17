package com.falanero.katanamod.component;

import com.falanero.katanamod.KatanaMod;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Components {

    public static final ComponentType<Integer> SOUL_COUNT_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(KatanaMod.MOD_ID, "soul_count"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static final ComponentType<Integer> HIT_COUNT_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(KatanaMod.MOD_ID, "hit_count"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static void initialize() {
        KatanaMod.LOGGER.info("Registering {} components", KatanaMod.MOD_ID);
    }
}