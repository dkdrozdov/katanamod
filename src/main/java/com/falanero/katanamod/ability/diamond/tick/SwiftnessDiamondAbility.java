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
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

import static com.falanero.katanamod.util.Souls.getCurrentLevel;
import static com.falanero.katanamod.util.Utility.arithmeticProgression;
import static com.falanero.katanamod.util.Utility.toRoman;

public class SwiftnessDiamondAbility extends Ability<PlayerEntityTickCallback> {

    public void appendTooltip(int itemLevel, Consumer<Text> tooltip) {
        int abilityLevel = getAbilityLevel(itemLevel);
        if (abilityLevel < 1)
            return;

        tooltip.accept(Text.translatable("item.katanamod.diamond_katana.swiftness.title", toRoman(abilityLevel)).formatted(Formatting.BOLD));
        tooltip.accept(Text.translatable("item.katanamod.diamond_katana.swiftness.description", toRoman(abilityLevel)));
    }

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

    public void apply(PlayerEntity player, int abilityLevel) {
        int effectLevel = abilityLevel - 1;

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
    public Text getDescription() {
        return Text.translatable("katanamod.ability.diamond.swiftness.description");
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
