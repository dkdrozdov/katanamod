package com.falanero.katanamod.item;

import com.falanero.katanamod.KatanaMod;
import com.falanero.katanamod.component.Components;
import com.falanero.katanamod.item.katana.DiamondKatanaItem;
import com.falanero.katanamod.item.katana.EmeraldKatanaItem;
import com.falanero.katanamod.item.katana.IronKatanaItem;
import com.falanero.katanamod.item.soulgem.DiamondSoulgemItem;
import com.falanero.katanamod.katana.DiamondKatana;
import com.falanero.katanamod.katana.IronKatana;
import com.falanero.katanamod.katana.Katanas;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;

import java.util.function.Function;

public class Items {

    public static Item registerItem(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // Create the item key.
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(KatanaMod.MOD_ID, name));

        // Create the item instance.
        Item item = itemFactory.apply(settings.registryKey(itemKey));

        // Register the item.
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

    public static final Item IRON_KATANA = registerItem("iron_katana",
            settings -> new IronKatanaItem(4, -2.9f, settings, Katanas.IRON_KATANA),
            new Item.Settings()
                    .component(Components.SOUL_COUNT_COMPONENT, 0)
                    .component(Components.HIT_COUNT_COMPONENT, 0)
                    .component(DataComponentTypes.UNBREAKABLE, Unit.INSTANCE));
    public static final Item DIAMOND_KATANA = registerItem("diamond_katana",
            settings -> new DiamondKatanaItem(4, -2.9f, settings, Katanas.DIAMOND_KATANA),
            new Item.Settings()
                    .component(Components.SOUL_COUNT_COMPONENT, 0)
                    .component(Components.HIT_COUNT_COMPONENT, 0)
                    .component(DataComponentTypes.UNBREAKABLE, Unit.INSTANCE));

    public static final Item EMERALD_KATANA = registerItem("emerald_katana",
            settings -> new EmeraldKatanaItem(4, -2.9f, settings, Katanas.EMERALD_KATANA),
            new Item.Settings()
                    .component(Components.SOUL_COUNT_COMPONENT, 0)
                    .component(Components.HIT_COUNT_COMPONENT, 0)
                    .component(DataComponentTypes.UNBREAKABLE, Unit.INSTANCE));

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register((itemGroup) -> itemGroup.add(IRON_KATANA));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register((itemGroup) -> itemGroup.add(DIAMOND_KATANA));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register((itemGroup) -> itemGroup.add(EMERALD_KATANA));
    }
}
