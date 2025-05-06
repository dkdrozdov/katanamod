package com.falanero.katanamod.registry.enities;

import com.falanero.katanamod.entity.FeatherbladeEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class Instances {
    public static final EntityType<FeatherbladeEntity> FEATHERBLADE_ENTITY =
            FabricEntityTypeBuilder.<FeatherbladeEntity>create(SpawnGroup.MISC, FeatherbladeEntity::new).dimensions(EntityDimensions.fixed(0.25f, 0.25f)).trackRangeBlocks(4).trackedUpdateRate(10).build();

}
