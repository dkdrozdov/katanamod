package com.falanero.katanamod.ability.diamond.tick;

import com.falanero.katanamod.ability.Ability;
import com.falanero.katanamod.callback.PlayerEntityTickCallback;
import com.falanero.katanamod.item.Items;
import com.falanero.katanamod.item.katana.KatanaItem;
import com.falanero.katanamod.util.itemStackData.KatanamodItemStackData;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.falanero.katanamod.util.Souls.getCurrentLevel;
import static com.falanero.katanamod.util.Utility.toRoman;

public class SwiftnessDiamondAbility extends Ability<PlayerEntityTickCallback> {
    @Override
    public Event<PlayerEntityTickCallback> getEvent() {
        return PlayerEntityTickCallback.EVENT;
    }

    @Override
    public PlayerEntityTickCallback getFunction() {
        return this::apply;
    }

    private void apply(PlayerEntity player) {
        if (player == null) return;

        ItemStack stack = getKatanaItem().getKatanaStack(player, null);
        if (stack == null) return;

        int level = getCurrentLevel(KatanamodItemStackData.getSoulCount(stack));
        int abilityLevel = getAbilityLevel(level);
        if (abilityLevel < 1) return;

        if (player.getWorld() instanceof ServerWorld) {
            apply(player, abilityLevel);
        }
    }

    private int getEffectLevel(int abilityLevel) {
        return abilityLevel - 1;
    }

    public void apply(PlayerEntity player, int abilityLevel) {
        int effectLevel = getEffectLevel(abilityLevel);

        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.SPEED,
                2,
                effectLevel,
                false,
                false,
                true));
    }

    @Override
    public KatanaItem getKatanaItem() {
        return (KatanaItem) Items.DIAMOND_KATANA;
    }

    @Override
    public Identifier getIconTexture() {
        return Identifier.ofVanilla("textures/mob_effect/speed.png");
    }

    @Override
    public Text getName() {
        return Text.translatable("katanamod.ability.diamond.swiftness.name");
    }

    @Override
    public Text getGenericDescription() {
        return Text.translatable("katanamod.ability.diamond.swiftness.description.generic");
    }

    @Override
    public Text getDetailedDescription(int abilityLevel) {
        return Text.translatable("katanamod.ability.diamond.swiftness.description.detailed", toRoman(getEffectLevel(abilityLevel)+1));
    }

    @Override
    public int getStartingLevel() {
        return 1;
    }

    @Override
    public int getIncrementLevel() {
        return 2;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

}
