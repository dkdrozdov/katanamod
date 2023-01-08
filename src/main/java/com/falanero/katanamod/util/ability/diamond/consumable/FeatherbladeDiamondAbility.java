package com.falanero.katanamod.util.ability.diamond.consumable;

import com.falanero.katanamod.entity.FeatherbladeEntity;
import com.falanero.katanamod.util.ability.ConsumableAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.tools.obfuscation.mapping.fg3.MappingMethodLazy;

import java.util.List;

import static com.falanero.katanamod.util.Utility.arithmeticProgression;
import static com.falanero.katanamod.util.Utility.toRoman;

public class FeatherbladeDiamondAbility {
    public static ConsumableAbility getAbility(){
        return FeatherbladeDiamondAbility::apply;
    }
    private static int getLevel(int itemLevel) {
        return arithmeticProgression(1, 2, 10, itemLevel);
    }
    public static void appendTooltip(int itemLevel, List<Text> tooltip) {
        int abilityLevel = getLevel(itemLevel);
        if (abilityLevel < 1)
            return;

        tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability.featherblade.title", toRoman(abilityLevel)).formatted(Formatting.BOLD));
        tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability.featherblade.description.line_1"));
        tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability.featherblade.description.line_2"));
    }

    public static boolean apply(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.getItemCooldownManager().set(itemStack.getItem(), 5);

        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 1F); // plays a globalSoundEvent

        if (!world.isClient) {
            FeatherbladeEntity featherbladeEntity = new FeatherbladeEntity(world, user);
            featherbladeEntity.setItem(itemStack);
            featherbladeEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 0F);
            world.spawnEntity(featherbladeEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1); // decrements itemStack if user is not in creative mode
        }

        return true;
    }
}
