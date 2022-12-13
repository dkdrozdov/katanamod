package com.falanero.katanamod.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import static com.falanero.katanamod.KatanaMod.MOD_ID;

public class Nbt {
    private static final String SOUL_COUNT_NBT = "soul_count";
    private static final String HIT_COUNT_NBT = "hit_count";

    static public NbtCompound createNbtIfAbsent(ItemStack stack){
        if(!stack.hasNbt()){
            stack.setNbt(new NbtCompound());
        }
        return stack.getOrCreateSubNbt(MOD_ID);
    }

    static private int getNbt(ItemStack stack, String nbtName){
        int value = 0;
        NbtCompound nbt = createNbtIfAbsent(stack);

        if(!nbt.contains(nbtName)){
            nbt.putInt(nbtName, value);
        }
        else{
            value = nbt.getInt(nbtName);
        }

        return value;
    }

    static private void setNbt(ItemStack stack, String nbtName, int value) {
        stack.getOrCreateSubNbt(MOD_ID).putInt(nbtName, value);
    }

    static public int getSoulCount(ItemStack stack){
        return getNbt(stack, SOUL_COUNT_NBT);
    }

    static public int getHitCount(ItemStack stack){
        return getNbt(stack, HIT_COUNT_NBT);
    }

    public static void setSoulCount(ItemStack stack, int soulCount){
        setNbt(stack, SOUL_COUNT_NBT, soulCount);
    }

    static public void setHitCount(ItemStack stack, int hitCount){
        setNbt(stack, HIT_COUNT_NBT, hitCount);
    }
}
