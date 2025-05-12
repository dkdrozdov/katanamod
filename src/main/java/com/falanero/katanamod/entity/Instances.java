package com.falanero.katanamod.entity;

import com.falanero.katanamod.KatanaMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class Instances {
//    public static final EntityType<FeatherbladeEntity> FEATHERBLADE_ENTITY =
//            registerEntity("featherblade", EntityType.Builder.create(FeatherbladeEntity::new, SpawnGroup.MISC).dropsNothing().dimensions(0.25f, 0.25f).maxTrackingRange(4).trackingTickInterval(10));

    public static <T extends Entity> EntityType<T> registerEntity(String name, EntityType.Builder<T> type) {
        RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(KatanaMod.MOD_ID, name));
        return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
    }

}
