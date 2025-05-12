package com.falanero.katanamod.util.itemStackData;

import net.minecraft.item.ItemStack;

public class DataComponentItemStackDataProvider implements ItemStackDataProvider {
    @Override
    public int getDataOrDefault(ItemStack stack, String key, int def) {
        return 0;
    }

    @Override
    public void setData(ItemStack stack, String key, int value) {

    }
}
