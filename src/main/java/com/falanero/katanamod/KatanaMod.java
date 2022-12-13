package com.falanero.katanamod;

import com.falanero.katanamod.registry.RegistryRecords;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KatanaMod implements ModInitializer {
    public static final String MOD_ID = "katanamod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        for (RegistryRecords record : RegistryRecords.values()) {
            Registry.register(
                    record.record.registry(),
                    new Identifier(MOD_ID, record.record.itemId()),
                    record.record.instance());
        }
    }
}
