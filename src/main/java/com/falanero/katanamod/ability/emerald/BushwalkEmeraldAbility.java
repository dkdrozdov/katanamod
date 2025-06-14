package com.falanero.katanamod.ability.emerald;

import com.falanero.katanamod.callback.BlockCollisionCheckCallback;
import com.falanero.katanamod.ability.Ability;
import com.falanero.katanamod.item.Items;
import com.falanero.katanamod.item.katana.KatanaItem;
import com.falanero.katanamod.util.itemStackData.KatanamodItemStackData;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static com.falanero.katanamod.util.Souls.getCurrentLevel;

public class BushwalkEmeraldAbility extends Ability<BlockCollisionCheckCallback> {

    @Override
    public Event<BlockCollisionCheckCallback> getEvent() {
        return BlockCollisionCheckCallback.COLLISION_CHECK_CALLBACK_EVENT;
    }

    @Override
    public BlockCollisionCheckCallback getFunction() {
        return this::apply;
    }

    private boolean apply(boolean original, Entity entity, BlockState blockState) {

        if (entity instanceof PlayerEntity player) {
            if (!getKatanaItem().isHeldBy(player, null)) return original;

            int itemLevel = getCurrentLevel(KatanamodItemStackData.getSoulCount(getKatanaItem().getKatanaStack(player, null)));
            int abilityLevel = getAbilityLevel(itemLevel);
            if (abilityLevel < 1) return original;

            return !shouldBypass(blockState);
        }

        return original;
    }

    private boolean shouldBypass(BlockState blockState) {
        return blockState.isIn(BlockTags.LEAVES);
    }

    @Override
    public KatanaItem getKatanaItem() {
        return (KatanaItem) Items.EMERALD_KATANA;
    }

    @Override
    public Identifier getIconTexture() {
        return Identifier.ofVanilla("textures/block/azalea_leaves.png");
    }

    @Override
    public Text getName() {
        return Text.translatable("katanamod.ability.emerald.bushwalk.name");
    }

    @Override
    public Text getGenericDescription() {
        return Text.translatable("katanamod.ability.emerald.bushwalk.description.generic");
    }

    @Override
    public Text getDetailedDescription(int abilityLevel) {
        return Text.translatable("katanamod.ability.emerald.bushwalk.description.detailed");
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
