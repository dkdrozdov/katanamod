package com.falanero.katanamod.util.itemStackData;

import net.minecraft.item.ItemStack;

public interface ItemStackDataProvider {
    /**
     * Gets int data from item stack. If there is no data, puts a default value in it and returns it.
     */
    int getDataOrDefault(ItemStack stack, String key, int def);

    /**
     * Sets int data to item stack.
     */
    void setData(ItemStack stack, String key, int value);
}
