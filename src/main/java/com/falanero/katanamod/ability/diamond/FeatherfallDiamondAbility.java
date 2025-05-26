package com.falanero.katanamod.ability.diamond;

import com.falanero.katanamod.ability.Ability;
import com.falanero.katanamod.callback.OnComputeFallDamage;
import com.falanero.katanamod.item.Items;
import com.falanero.katanamod.item.katana.KatanaItem;
import com.falanero.katanamod.util.itemStackData.KatanamodItemStackData;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import static com.falanero.katanamod.util.Souls.getCurrentLevel;

public class FeatherfallDiamondAbility extends Ability<OnComputeFallDamage> {

    private float getReduction(int abilityLevel) {
        return 1f - (4 + 7 * (float) abilityLevel) / 100f;
    }


    @Override
    public Event<OnComputeFallDamage> getEvent() {
        return OnComputeFallDamage.ON_COMPUTE_FALL_DAMAGE_CALLBACK_EVENT;
    }

    @Override
    public OnComputeFallDamage getFunction() {
        return this::apply;
    }

    private Pair<Boolean, Integer> apply(int fallDamage, double fallDistance, float damageMultiplier, LivingEntity entity) {
        if (entity instanceof PlayerEntity player && getKatanaItem().isHeldBy(player, null)) {
            int itemLevel = getCurrentLevel(KatanamodItemStackData.getSoulCount(getKatanaItem().getKatanaStack(player, null)));
            int abilityLevel = getAbilityLevel(itemLevel);
            if (abilityLevel < 1) return new ImmutablePair<>(false, fallDamage);

            return onComputeFallDamage(fallDamage, fallDistance, damageMultiplier, entity, abilityLevel);
        }
        return new ImmutablePair<>(false, fallDamage);
    }

    private Pair<Boolean, Integer> onComputeFallDamage(int fallDamage, double fallDistance, float damageMultiplier, LivingEntity entity, int abilityLevel) {
        if (entity.isSneaking()) {
//            int rawDamage = MathHelper.ceil((fallDistance - 3.0f) * damageMultiplier);
//            float reductionFactor = getReduction(abilityLevel);
//            return new ImmutablePair<>(true, Math.min((int) Math.floor(rawDamage * reductionFactor), fallDamage));
            return new ImmutablePair<>(true, 0);
        }
        return new ImmutablePair<>(false, fallDamage);
    }

    @Override
    public KatanaItem getKatanaItem() {
        return (KatanaItem) Items.DIAMOND_KATANA;
    }

    @Override
    public Identifier getIconTexture() {
        return Identifier.ofVanilla("textures/mob_effect/slow_falling.png");
    }

    @Override
    public Text getName() {
        return Text.translatable("katanamod.ability.diamond.featherfall.name");
    }

    @Override
    public Text getGenericDescription() {
        return Text.translatable("katanamod.ability.diamond.featherfall.description.generic");
    }

    @Override
    public Text getDetailedDescription(int abilityLevel) {
        return Text.translatable("katanamod.ability.diamond.featherfall.description.detailed");
    }

    @Override
    public int getStartingLevel() {
        return 5;
    }

    @Override
    public int getIncrementLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
