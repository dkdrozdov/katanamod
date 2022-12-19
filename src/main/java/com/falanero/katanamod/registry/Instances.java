package com.falanero.katanamod.registry;

import com.falanero.katanamod.entity.FeatherbladeEntity;
import com.falanero.katanamod.item.katana.DiamondKatanaItem;
import com.falanero.katanamod.item.katana.IronKatanaItem;
import com.falanero.katanamod.item.katana.KatanaItem;
import com.falanero.katanamod.item.soulgem.DiamondSoulgemItem;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class Instances {
    public static final KatanaItem IRON_KATANA = new IronKatanaItem(
            4,
            -2.9f,
            Settings.IRON_KATANA);

    public static final DiamondKatanaItem DIAMOND_KATANA = new DiamondKatanaItem(
            4,
            -2.9f,
            Settings.DIAMOND_KATANA);

    public static final DiamondSoulgemItem DIAMOND_SOULGEM = new DiamondSoulgemItem(
            Settings.DIAMOND_SOULGEM);

    public static final EntityType<FeatherbladeEntity> FEATHERBLADE_ENTITY =
            FabricEntityTypeBuilder.<FeatherbladeEntity>create(SpawnGroup.MISC, FeatherbladeEntity::new).dimensions(EntityDimensions.fixed(0.25f, 0.25f)).trackRangeBlocks(4).trackedUpdateRate(10).build();

}
