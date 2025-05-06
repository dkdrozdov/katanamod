package com.falanero.katanamod.registry.items;

import com.falanero.katanamod.item.katana.DiamondKatanaItem;
import com.falanero.katanamod.item.katana.IronKatanaItem;
import com.falanero.katanamod.item.katana.KatanaItem;
import com.falanero.katanamod.item.soulgem.DiamondSoulgemItem;

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

}
