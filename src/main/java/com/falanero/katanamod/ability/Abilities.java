package com.falanero.katanamod.ability;

import com.falanero.katanamod.ability.diamond.FeatherfallDiamondAbility;
import com.falanero.katanamod.ability.diamond.WeaveDiamondAbility;
import com.falanero.katanamod.ability.diamond.attack.SkyboundDiamondAbility;
import com.falanero.katanamod.ability.diamond.tick.SpringDiamondAbility;
import com.falanero.katanamod.ability.diamond.tick.SwiftnessDiamondAbility;

public class Abilities {
    public static final Ability<?> SKYBOUND_DIAMOND_ABILITY = new SkyboundDiamondAbility();
    public static final Ability<?> SPRING_DIAMOND_ABILITY = new SpringDiamondAbility();
    public static final Ability<?> SWIFTNESS_DIAMOND_ABILITY = new SwiftnessDiamondAbility();
    public static final Ability<?> FEATHERFALL_DIAMOND_ABILITY = new FeatherfallDiamondAbility();
    public static final Ability<?> WEAVE_DIAMOND_ABILITY = new WeaveDiamondAbility();
}
