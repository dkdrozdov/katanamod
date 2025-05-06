package com.falanero.katanamod.util.itemStackData;

import net.minecraft.item.ItemStack;

public class KatanamodItemStackData {
    private static final ItemStackDataProvider dataProvider = new NbtItemStackDataProvider();
    private static final String SOUL_COUNT = "soul_count";
    private static final String HIT_COUNT = "hit_count";

    static public int getSoulCount(ItemStack stack){
        return dataProvider.getDataOrDefault(stack, SOUL_COUNT, 0);
    }

    static public int getHitCount(ItemStack stack){
        return dataProvider.getDataOrDefault(stack, HIT_COUNT, 0);
    }

    public static void setSoulCount(ItemStack stack, int soulCount){
        dataProvider.setData(stack, SOUL_COUNT, soulCount);
    }

    static public void setHitCount(ItemStack stack, int hitCount){
        dataProvider.setData(stack, HIT_COUNT, hitCount);
    }
}
