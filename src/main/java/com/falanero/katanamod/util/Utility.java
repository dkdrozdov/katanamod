package com.falanero.katanamod.util;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Hand;

public class Utility {
    static public Hand GetHandByEquipmentSlot(EquipmentSlot slot){
        if(slot.getType() != EquipmentSlot.Type.HAND)
            return null;
        return slot == EquipmentSlot.MAINHAND ? Hand.MAIN_HAND : Hand.OFF_HAND;
    }
}
