//package com.falanero.katanamod.ability.diamond.tick;
//
//import com.falanero.katanamod.ability.TickAbility;
//import net.minecraft.entity.effect.StatusEffectInstance;
//import net.minecraft.entity.effect.StatusEffects;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.text.Text;
//import net.minecraft.util.Formatting;
//
//import java.util.List;
//
//import static com.falanero.katanamod.util.Utility.arithmeticProgression;
//import static com.falanero.katanamod.util.Utility.toRoman;
//
//public class SpringDiamondAbility {
//    private static int getLevel(int itemLevel) {
//        return arithmeticProgression(1, 2, 10, itemLevel);
//    }
//
//    public static void appendTooltip(int itemLevel, List<Text> tooltip) {
//        int abilityLevel = getLevel(itemLevel);
//        if (abilityLevel < 1)
//            return;
//
//        tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability.spring.title", toRoman(abilityLevel)).formatted(Formatting.BOLD));
//        tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability.spring.description", toRoman(abilityLevel)));
//    }
//
//    public static TickAbility getAbility(){
//        return SpringDiamondAbility::apply;
//    }
//
//    public static void apply(PlayerEntity player, int itemLevel) {
//        int abilityLevel = getLevel(itemLevel);
//        if (abilityLevel < 1)
//            return;
//
//        if (!player.world.isClient) {
//            if (player.isSneaking()) {
//                int effectLevel;
//                int effectTickTime;
//                effectLevel = (abilityLevel - 1) / 2;
//                effectTickTime = 10;
//
//                player.addStatusEffect(new StatusEffectInstance(
//                        StatusEffects.JUMP_BOOST,
//                        effectTickTime,
//                        effectLevel,
//                        false,
//                        false,
//                        true));
//            }
//        }
//    }
//}
