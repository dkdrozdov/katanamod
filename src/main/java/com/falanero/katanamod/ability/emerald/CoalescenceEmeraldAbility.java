package com.falanero.katanamod.ability.emerald;

import com.falanero.katanamod.ability.Ability;
import com.falanero.katanamod.callback.PlayerEntityTickCallback;
import com.falanero.katanamod.item.Items;
import com.falanero.katanamod.item.katana.KatanaItem;
import com.falanero.katanamod.util.Souls;
import com.falanero.katanamod.util.itemStackData.KatanamodItemStackData;
import com.jcraft.jorbis.Block;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

import static com.falanero.katanamod.util.Souls.getCurrentLevel;
import static com.falanero.katanamod.util.Souls.getSoulsForLevel;

public class CoalescenceEmeraldAbility extends Ability<PlayerEntityTickCallback> {
    private static final List<BlockPos> GRASS_POINTS_PROVIDER_OFFSETS = BlockPos.stream(-2, -1, -2, 2, 1, 2)
            .map(BlockPos::toImmutable)
            .toList();

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
        if (stack != null) {
            int level = getCurrentLevel(KatanamodItemStackData.getSoulCount(stack));
            int abilityLevel = getAbilityLevel(level);
            if (abilityLevel < 1)
                return;
            if (player.getWorld() instanceof ServerWorld) {
                apply(player, abilityLevel);
            }
        }
    }

    private void apply(PlayerEntity player, int abilityLevel) {

        if (player instanceof ServerPlayerEntity serverPlayerEntity) {
            float grassPoints = getBlockGrassPoints(player);
            if (grassPoints > 0) {
                int effectLevel = getEffectLevel(abilityLevel, grassPoints);
                int effectTickTime = 51;
                if (serverPlayerEntity.age % 5 == 0) {

                    var effects = player.getStatusEffects();

                    if (effects.stream().noneMatch(statusEffectInstance ->
                            statusEffectInstance.getEffectType() == StatusEffects.REGENERATION &&
                                    statusEffectInstance.getAmplifier() >= effectLevel)) {
                        player.addStatusEffect(new StatusEffectInstance(
                                StatusEffects.REGENERATION,
                                effectTickTime,
                                effectLevel,
                                false,
                                false,
                                true));
                    }
                }
            }
        }
    }

    private float getBlockGrassPoints(World world, BlockPos playerPos, BlockPos providerOffset) {
        var blockState = world.getBlockState(playerPos.add(providerOffset));

        // LEAVES
        if (blockState.isIn(BlockTags.LEAVES)) return 0.008f;
        if (blockState.isIn(BlockTags.BAMBOO_BLOCKS)) return 0.009f;
        if (blockState.isIn(BlockTags.CRIMSON_STEMS)) return 0.0095f;
        if (blockState.isIn(BlockTags.CAVE_VINES)) return 0.0095f;

        // VINES
        if (blockState.isOf(Blocks.VINE)) return 0.018f;

        // GRASS
        if (blockState.isIn(BlockTags.CROPS)) return 0.021f;
        if (blockState.isIn(BlockTags.SAPLINGS)) return 0.03f;
        if (blockState.isOf(Blocks.SWEET_BERRY_BUSH)) return 0.03f;
        if (blockState.isOf(Blocks.TALL_GRASS)) return 0.015f;
        if (blockState.isOf(Blocks.SUGAR_CANE)) return 0.015f;
        if (blockState.isOf(Blocks.SHORT_GRASS)) return 0.0205f;
        if (blockState.isOf(Blocks.PUMPKIN)) return 0.016f;
        if (blockState.isOf(Blocks.PUMPKIN_STEM)) return 0.016f;
        if (blockState.isOf(Blocks.MELON)) return 0.016f;
        if (blockState.isOf(Blocks.MELON_STEM)) return 0.016f;
        if (blockState.isIn(BlockTags.SMALL_FLOWERS)) return 0.031f;
        if (blockState.isIn(BlockTags.FLOWERS)) return 0.016f;
        if (blockState.isOf(Blocks.CACTUS)) return 0.016f;
        if (blockState.isOf(Blocks.CACTUS_FLOWER)) return 0.016f;

        return 0;
    }

    private float getBlockGrassPoints(PlayerEntity player) {
        float grassPoints = 0;
        for (BlockPos blockPos : GRASS_POINTS_PROVIDER_OFFSETS) {
            grassPoints += getBlockGrassPoints(player.getWorld(), player.getBlockPos(), blockPos);
        }
//        if (player instanceof ServerPlayerEntity serverPlayerEntity)
//            serverPlayerEntity.sendMessage(Text.literal(String.valueOf(grassPoints)), true);

        return grassPoints;
    }

    private int getEffectLevel(int abilityLevel, float grassPoints) {
        return (int) (grassPoints * abilityLevel * 3 - 1);
    }

    @Override
    public KatanaItem getKatanaItem() {
        return (KatanaItem) Items.EMERALD_KATANA;
    }

    @Override
    public Identifier getIconTexture() {
        return Identifier.ofVanilla("textures/mob_effect/regeneration.png");
    }

    @Override
    public Text getName() {
        return Text.translatable("katanamod.ability.emerald.coalescence.name");
    }

    @Override
    public Text getGenericDescription() {
        return Text.translatable("katanamod.ability.emerald.coalescence.description.generic");
    }

    @Override
    public Text getDetailedDescription(int abilityLevel) {
        return Text.translatable("katanamod.ability.emerald.coalescence.description.detailed");
    }

    @Override
    public int getStartingLevel() {
        return 2;
    }

    @Override
    public int getIncrementLevel() {
        return 2;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }
}
