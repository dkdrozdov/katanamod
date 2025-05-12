package com.falanero.katanamod;

import com.falanero.katanamod.component.Components;
import com.falanero.katanamod.item.Items;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KatanaMod implements ModInitializer {
    public static final String MOD_ID = "katanamod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        Items.initialize();
//        Entities.register();
        Components.initialize();
        KatanaMod.LOGGER.info("mod initialized");
    }
}
