//package com.falanero.katanamod.ability.diamond.attack;
//
//import com.falanero.katanamod.KatanaMod;
//import com.falanero.katanamod.registry.IDs;
//import com.falanero.katanamod.ability.AttackAbility;
//import com.falanero.katanamod.util.itemStackData.KatanamodItemStackData;
//import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
//import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
//import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.attribute.EntityAttributes;
//import net.minecraft.entity.effect.StatusEffectInstance;
//import net.minecraft.entity.effect.StatusEffects;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.network.PacketByteBuf;
//import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
//import net.minecraft.server.network.ServerPlayerEntity;
//import net.minecraft.server.world.ServerWorld;
//import net.minecraft.text.Text;
//import net.minecraft.util.Formatting;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Vec3d;
//
//import java.util.List;
//
//import static com.falanero.katanamod.util.Utility.arithmeticProgression;
//import static com.falanero.katanamod.util.Utility.toRoman;
//
//public class SkyboundDiamondAbility {
//    private static int getLevel(int itemLevel) {
//        return arithmeticProgression(2, 2, 6, itemLevel);
//    }
//
//    public static void appendTooltip(int itemLevel, List<Text> tooltip) {
//        int abilityLevel = getLevel(itemLevel);
//        if (abilityLevel < 1)
//            return;
//        tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability.skybound.title", toRoman(abilityLevel)).formatted(Formatting.BOLD));
//        tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability.skybound.description.line_1", getAttacksToTrigger(abilityLevel), toRoman(getJumpBoostLevel(abilityLevel))));
//        tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability.skybound.description.line_2", getJumpBoostTime(abilityLevel), getThrowHeight(abilityLevel)));
//    }
//
//    /**
//     * @return duration time in seconds.
//     */
//    private static float getJumpBoostTime(int abilityLevel) {
//        return (float) (abilityLevel * 0.5);
//    }
//
//    private static int getJumpBoostLevel(int abilityLevel) {
//        return abilityLevel;
//    }
//
//    private static float getThrowHeight(int abilityLevel) {
//        return 4 + (abilityLevel - 1);
//    }
//
//    private static int getAttacksToTrigger(int abilityLevel) {
//        return Math.max(7 - abilityLevel + 1, 3);
//    }
//
//    private static boolean isTriggered(int abilityLevel, int hitCount) {
//        int currentHitCountTrigger = getAttacksToTrigger(abilityLevel);
//
//        return hitCount >= currentHitCountTrigger;
//    }
//
//    public static AttackAbility getAbility(){
//        return SkyboundDiamondAbility::apply;
//    }
//    private static void applyAbility(LivingEntity target, LivingEntity player, int abilityLevel) {
//        int jumpBoostLevel = getJumpBoostLevel(abilityLevel) - 1;
//        int jumpBoostTimeTicks = (int) (getJumpBoostTime(abilityLevel) * 20);
//        double targetKnockbackResistance = Math.max(0.0, 1.0 - target.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE));
//        double playerKnockbackResistance = Math.max(0.0, 1.0 - player.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE));
//        double throwVelocity = Math.sqrt(2 * (1.75 + getThrowHeight(abilityLevel)) * LivingEntity.GRAVITY) * Math.max(targetKnockbackResistance * playerKnockbackResistance, 0.15);
////            ((1+getThrowHeight(abilityLevel)) * ((LivingEntityInvoker)target).invokeGetJumpVelocity() * Math.max(targetKnockbackResistance, 0.15));
//        Vec3d velocityVector = new Vec3d(0.0, throwVelocity, 0.0);
//        PacketByteBuf passedData = PacketByteBufs.create();
//        passedData.writeDouble(velocityVector.x);
//        passedData.writeDouble(velocityVector.y);
//        passedData.writeDouble(velocityVector.z);
//
//        player.addStatusEffect(new StatusEffectInstance(
//                StatusEffects.JUMP_BOOST,
//                jumpBoostTimeTicks,
//                jumpBoostLevel));
//
//        player.setVelocity(velocityVector);
//        target.setVelocity(velocityVector);
//        player.velocityModified = true;
//        target.velocityModified = true;
//        if (target instanceof PlayerEntity)((ServerPlayerEntity)target).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(target));
//        ((ServerPlayerEntity)player).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
//        player.velocityModified = false;        target.velocityModified = false;
//
//
//        BlockPos targetPos = new BlockPos(target.getPos());
//        BlockPos playerPos = new BlockPos(player.getPos());
//        PacketByteBuf targetBuf = PacketByteBufs.create();
//        PacketByteBuf playerBuf = PacketByteBufs.create();
//        targetBuf.writeBlockPos(targetPos);
//        playerBuf.writeBlockPos(playerPos);
//
////        KatanaMod.LOGGER.info("applying SkyboundDiamondAbility. Sending packets to :");
//        for (ServerPlayerEntity serverPlayerEntity : PlayerLookup.tracking((ServerWorld) player.world, targetPos)) {
//            ServerPlayNetworking.send(serverPlayerEntity, IDs.SKYBOUND_S2C_PACKET_ID, targetBuf);
////            KatanaMod.LOGGER.info("{}", serverPlayerEntity.getName().getString());
//        }
////        KatanaMod.LOGGER.info("applying SkyboundDiamondAbility. Packets sent.");
//
//        //for (ServerPlayerEntity serverPlayerEntity : PlayerLookup.tracking(player)) {
//        //    ServerPlayNetworking.send(serverPlayerEntity, KatanaMod.SKYBOUND_S2C_PACKET_ID, playerBuf);
//        //}
//        //ServerPlayNetworking.send((ServerPlayerEntity) player, KatanaMod.SKYBOUND_S2C_PACKET_ID, passedData);
////        if (target instanceof PlayerEntity) {
////            ServerPlayNetworking.send((ServerPlayerEntity) target, KatanaMod.SET_VELOCITY_PACKET_ID, passedData);
////        }
//    }
//
//    static void apply(ItemStack stack, LivingEntity target, LivingEntity attacker, int itemLevel) {
//        if(attacker.world.isClient){
//            KatanaMod.LOGGER.info("Tried to apply SkyboundDiamondAbility in client thread.");
//            return;
//        }
//
//        int abilityLevel = getLevel(itemLevel);
//
//        if (abilityLevel < 1)
//            return;
//
//        int hitCount = KatanamodItemStackData.getHitCount(stack) + 1;
//        if (isTriggered(abilityLevel, hitCount)) {
//            hitCount = 0;
//            applyAbility(target, attacker, abilityLevel);
//        }
//        KatanamodItemStackData.setHitCount(stack, hitCount);
//    }
//}