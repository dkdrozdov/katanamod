package com.falanero.katanamod.katana;

import java.util.ArrayList;
import java.util.List;

public class Katanas {
    public static final Katana IRON_KATANA = new IronKatana();
    public static final Katana DIAMOND_KATANA = new DiamondKatana();
    public static final Katana EMERALD_KATANA = new EmeraldKatana();

    public static final List<Katana> katanas = List.of(
            IRON_KATANA,
            DIAMOND_KATANA,
            EMERALD_KATANA);
}
