//package com.falanero.katanamod.util.itemStackData;
//
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NbtCompound;
//
//import static com.falanero.katanamod.KatanaMod.MOD_ID;
//
//public class NbtItemStackDataProvider implements ItemStackDataProvider {
//
//    /**
//     * Gets mod nbt-component from the item stack or creates it if it doesn't exist.
//     */
//    static private NbtCompound getOrCreateNbt(ItemStack stack) {
//        if (!stack.hasNbt()) {
//            stack.setNbt(new NbtCompound());
//        }
//        return stack.getOrCreateSubNbt(MOD_ID);
//    }
//
//
//    /**
//     * Gets int data from item stack's mod nbt-component. If there is no data, puts a default value in it and returns it.
//     */
//    static private int getNbtOrDefault(ItemStack stack, String nbtName, int def) {
//        int value = def;
//        NbtCompound nbt = getOrCreateNbt(stack);
//
//        if (!nbt.contains(nbtName)) {
//            nbt.putInt(nbtName, value);
//        } else {
//            value = nbt.getInt(nbtName);
//        }
//
//        return value;
//    }
//
//    static private void setNbt(ItemStack stack, String nbtName, int value) {
//        stack.getOrCreateSubNbt(MOD_ID).putInt(nbtName, value);
//    }
//
//    @Override
//    public int getDataOrDefault(ItemStack stack, String key, int def) {
//        return getNbtOrDefault(stack, key, def);
//    }
//
//    @Override
//    public void setData(ItemStack stack, String key, int value) {
//        setNbt(stack, key, value);
//    }
//}
