package com.falanero.katanamod;

import com.falanero.katanamod.registry.enities.Entities;
import com.falanero.katanamod.registry.items.Items;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KatanaMod implements ModInitializer {
    public static final String MOD_ID = "katanamod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        Items.register();
        Entities.register();
        KatanaMod.LOGGER.info("mod initialized");
    }
}
