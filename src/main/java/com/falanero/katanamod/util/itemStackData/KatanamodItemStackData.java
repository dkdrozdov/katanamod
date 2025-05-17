package com.falanero.katanamod.util.itemStackData;

import com.falanero.katanamod.component.Components;
import net.minecraft.item.ItemStack;

import java.util.NoSuchElementException;

public class KatanamodItemStackData {
    private static final ItemStackDataProvider dataProvider = new DataComponentItemStackDataProvider();
    //    private static final String SOUL_COUNT = "soul_count";
    private static final String HIT_COUNT = "hit_count";

    static public int getSoulCount(ItemStack stack) {
        if (!stack.contains(Components.SOUL_COUNT_COMPONENT))
            throw new NoSuchElementException("Tried to access component that doesn't exist.");

        return stack.getOrDefault(Components.SOUL_COUNT_COMPONENT, 0);
    }

    static public int getHitCount(ItemStack stack) {
        if (!stack.contains(Components.HIT_COUNT_COMPONENT))
            throw new NoSuchElementException("Tried to access component that doesn't exist.");

        return stack.getOrDefault(Components.HIT_COUNT_COMPONENT, 0);
    }

    public static void setSoulCount(ItemStack stack, int soulCount) {
        if (!stack.contains(Components.SOUL_COUNT_COMPONENT))
            throw new NoSuchElementException("Tried to access component that doesn't exist.");

        stack.set(Components.SOUL_COUNT_COMPONENT, soulCount);
    }

    static public void setHitCount(ItemStack stack, int hitCount) {
        if (!stack.contains(Components.HIT_COUNT_COMPONENT))
            throw new NoSuchElementException("Tried to access component that doesn't exist.");

        stack.set(Components.HIT_COUNT_COMPONENT, hitCount);
    }
}
