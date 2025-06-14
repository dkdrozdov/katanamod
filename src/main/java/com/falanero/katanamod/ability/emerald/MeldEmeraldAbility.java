package com.falanero.katanamod.ability.emerald;

import com.falanero.katanamod.ability.Ability;
import com.falanero.katanamod.callback.OnBlockCollisionCallback;
import com.falanero.katanamod.item.Items;
import com.falanero.katanamod.item.katana.KatanaItem;
import com.falanero.katanamod.util.itemStackData.KatanamodItemStackData;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.falanero.katanamod.util.Souls.getCurrentLevel;

public class MeldEmeraldAbility extends Ability<OnBlockCollisionCallback> {

    @Override
    public Event<OnBlockCollisionCallback> getEvent() {
        return OnBlockCollisionCallback.ON_BLOCK_COLLISION_CALLBACK_EVENT;
    }

    @Override
    public OnBlockCollisionCallback getFunction() {
        return this::apply;
    }

    private void apply(Entity entity, BlockState blockState) {
        if (entity instanceof PlayerEntity player) {
            if (!getKatanaItem().isHeldBy(player, null)) return;
            if (!blockState.isIn(BlockTags.LEAVES)) return;

            int itemLevel = getCurrentLevel(KatanamodItemStackData.getSoulCount(getKatanaItem().getKatanaStack(player, null)));
            int abilityLevel = getAbilityLevel(itemLevel);
            if (abilityLevel < 1) return;

            if (player.getWorld() instanceof ServerWorld) {
                onTouchingLeaves(player, abilityLevel);
            }
        }
    }

    private void onTouchingLeaves(PlayerEntity player, int abilityLevel) {
        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.INVISIBILITY,
                3*20+5,
                0,
                false,
                false,
                true));
    }

    @Override
    public KatanaItem getKatanaItem() {
        return (KatanaItem) Items.EMERALD_KATANA;
    }

    @Override
    public Identifier getIconTexture() {
        return Identifier.ofVanilla("textures/mob_effect/invisibility.png");
    }

    @Override
    public Text getName() {
        return Text.translatable("katanamod.ability.emerald.meld.name");
    }

    @Override
    public Text getGenericDescription() {
        return Text.translatable("katanamod.ability.emerald.meld.description.generic");
    }

    @Override
    public Text getDetailedDescription(int abilityLevel) {
        return Text.translatable("katanamod.ability.emerald.meld.description.detailed");
    }

    @Override
    public int getStartingLevel() {
        return 1;
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
