package com.falanero.katanamod.ability;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.falanero.katanamod.util.Utility.arithmeticProgression;

public abstract class Ability<EventCallback> {
    public abstract Event<EventCallback> getEvent();

    public abstract EventCallback getFunction();

    public abstract com.falanero.katanamod.item.katana.KatanaItem getKatanaItem();

    public Ability() {
        getEvent().register(getFunction());
    }

    public abstract Identifier getIconTexture();

    public abstract Text getName();

    public abstract Text getGenericDescription();
    public abstract Text getDetailedDescription(int abilityLevel);

    public int getAbilityLevel(int katanaLevel) {
        return arithmeticProgression(getStartingLevel(), getIncrementLevel(), getMaxLevel(), katanaLevel);
    }

    public int getNeededLevelFor(int abilityLevel) {
        return getStartingLevel() + getIncrementLevel() * (abilityLevel - 1);
    }

    public abstract int getStartingLevel();

    public abstract int getIncrementLevel();

    public abstract int getMaxLevel();
}

