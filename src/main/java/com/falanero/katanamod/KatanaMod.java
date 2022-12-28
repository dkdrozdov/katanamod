package com.falanero.katanamod;

import com.falanero.katanamod.registry.RegistryEntries;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KatanaMod implements ModInitializer {
    public static final String MOD_ID = "katanamod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final RegistryEntries registryEntries = new RegistryEntries();
    public static final Identifier SKYBOUND_S2C_PACKET_ID = new Identifier("katanamod:diamond_skybound");
    public static final Identifier WINDBOMB_S2C_PACKET_ID = new Identifier("katanamod:diamond_windbomb");
    @Override
    public void onInitialize() {
        for (var record: registryEntries)
            record.register(MOD_ID);
    }
}
