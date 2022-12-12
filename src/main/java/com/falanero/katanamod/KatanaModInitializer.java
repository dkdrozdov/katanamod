package com.falanero.katanamod;

import com.falanero.katanamod.registry.RegistryRecords;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class KatanaModInitializer implements ModInitializer {
    public static final String MODID = "katanamod";

    @Override
    public void onInitialize() {
        for (RegistryRecords record : RegistryRecords.values()) {
            Registry.register(
                    record.record.registry(),
                    new Identifier(MODID, record.record.itemId()),
                    record.record.instance());
        }
    }
}
