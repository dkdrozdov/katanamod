package com.falanero.katanamod.ability.diamond;

import com.falanero.katanamod.ability.Ability;
import com.falanero.katanamod.callback.OnGetAirStrafingSpeedCallback;
import com.falanero.katanamod.item.Items;
import com.falanero.katanamod.item.katana.KatanaItem;
import com.falanero.katanamod.util.itemStackData.KatanamodItemStackData;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import static com.falanero.katanamod.util.Souls.getCurrentLevel;

public class WeaveDiamondAbility extends Ability<OnGetAirStrafingSpeedCallback> {

    @Override
    public Event<OnGetAirStrafingSpeedCallback> getEvent() {
        return OnGetAirStrafingSpeedCallback.ON_GET_AIR_STRAFING_SPEED_CALLBACK_EVENT;
    }

    @Override
    public OnGetAirStrafingSpeedCallback getFunction() {
        return this::apply;
    }

    private Pair<Boolean, Float> apply(float originalSpeed, PlayerEntity player) {
        if (!getKatanaItem().isHeldBy(player, null)) return new ImmutablePair<>(false, originalSpeed);

        int itemLevel = getCurrentLevel(KatanamodItemStackData.getSoulCount(getKatanaItem().getKatanaStack(player, null)));
        int abilityLevel = getAbilityLevel(itemLevel);
        if (abilityLevel < 1)
            return new ImmutablePair<>(false, originalSpeed);

        return onGetAirStrafingSpeed(originalSpeed, player, abilityLevel);
    }


    private static Pair<Boolean, Float> onGetAirStrafingSpeed(float original, PlayerEntity player, int abilityLevel) {
        var speed = player.getMovementSpeed();
        return new ImmutablePair<>(true, player.isSprinting() ? speed / 3.846f : speed / 5.0f);
    }

    @Override
    public KatanaItem getKatanaItem() {
        return (KatanaItem) Items.DIAMOND_KATANA;
    }

    @Override
    public Identifier getIconTexture() {
        return Identifier.ofVanilla("textures/mob_effect/levitation.png");
    }

    @Override
    public Text getName() {
        return Text.translatable("katanamod.ability.diamond.weave.name");
    }

    @Override
    public Text getGenericDescription() {
        return Text.translatable("katanamod.ability.diamond.weave.description.generic");
    }

    @Override
    public Text getDetailedDescription(int abilityLevel) {
        return Text.translatable("katanamod.ability.diamond.weave.description.detailed");
    }

    @Override
    public int getStartingLevel() {
        return 3;
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
