package com.falanero.katanamod.ability;

import com.falanero.katanamod.item.katana.KatanaItem;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class Ability<EventCallback> {
    public abstract Event<EventCallback> getEvent();
    public abstract EventCallback getFunction();
    public abstract com.falanero.katanamod.item.katana.KatanaItem getKatanaItem();

     public Ability(){
         getEvent().register(getFunction());
    }

    public abstract Identifier getIconTexture();

    public abstract Text getName();

    public abstract Text getDescription();
}

