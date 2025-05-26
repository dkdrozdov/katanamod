package com.falanero.katanamod.ability.diamond.attack;

import com.falanero.katanamod.KatanaMod;
import com.falanero.katanamod.ability.Ability;
import com.falanero.katanamod.callback.OnAttackCallback;
import com.falanero.katanamod.item.Items;
import com.falanero.katanamod.item.katana.DiamondKatanaItem;
import com.falanero.katanamod.util.itemStackData.KatanamodItemStackData;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import static com.falanero.katanamod.util.Souls.getCurrentLevel;
import static com.falanero.katanamod.util.Utility.toRoman;

public class SkyboundDiamondAbility extends Ability<OnAttackCallback> {
    /**
     * @return duration time in seconds.
     */
    private static float getJumpBoostTime(int abilityLevel) {
        return (float) (abilityLevel * 0.5);
    }

    private static int getJumpBoostLevel(int abilityLevel) {
        return abilityLevel - 1;
    }

    private static float getThrowHeight(int abilityLevel) {
        return 4 + (abilityLevel - 1);
    }

    private static int getAttacksToTrigger(int abilityLevel) {
        return Math.max(7 - abilityLevel + 1, 3);
    }


    private static void applyAbility(LivingEntity target, LivingEntity player, int abilityLevel) {
        int jumpBoostLevel = getJumpBoostLevel(abilityLevel);
        int jumpBoostTimeTicks = (int) (getJumpBoostTime(abilityLevel) * 20);
        double targetKnockbackResistance = Math.max(0.0, 1.0 - target.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE));
        double playerKnockbackResistance = Math.max(0.0, 1.0 - player.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE));
        double throwVelocity = Math.sqrt(2 * (1.75 + getThrowHeight(abilityLevel)) * LivingEntity.GRAVITY) * Math.max(targetKnockbackResistance * playerKnockbackResistance, 0.15);
//            ((1+getThrowHeight(abilityLevel)) * ((LivingEntityInvoker)target).invokeGetJumpVelocity() * Math.max(targetKnockbackResistance, 0.15));
        Vec3d velocityVector = new Vec3d(0.0, throwVelocity, 0.0);
        PacketByteBuf passedData = PacketByteBufs.create();
        passedData.writeDouble(velocityVector.x);
        passedData.writeDouble(velocityVector.y);
        passedData.writeDouble(velocityVector.z);

        player.addStatusEffect(new StatusEffectInstance(
                StatusEffects.JUMP_BOOST,
                jumpBoostTimeTicks,
                jumpBoostLevel));

        player.setVelocity(velocityVector);
        target.setVelocity(velocityVector);
        player.velocityModified = true;
        target.velocityModified = true;
        if (target instanceof PlayerEntity)
            ((ServerPlayerEntity) target).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(target));
        ((ServerPlayerEntity) player).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
        player.velocityModified = false;
        target.velocityModified = false;


        BlockPos targetPos = new BlockPos(target.getBlockPos());
        BlockPos playerPos = new BlockPos(player.getBlockPos());
        PacketByteBuf targetBuf = PacketByteBufs.create();
        PacketByteBuf playerBuf = PacketByteBufs.create();
        targetBuf.writeBlockPos(targetPos);
        playerBuf.writeBlockPos(playerPos);

//        KatanaMod.LOGGER.info("applying SkyboundDiamondAbility. Sending packets to :");
//        for (ServerPlayerEntity serverPlayerEntity : PlayerLookup.tracking((ServerWorld) player.world, targetPos)) {
//            ServerPlayNetworking.send(serverPlayerEntity, IDs.SKYBOUND_S2C_PACKET_ID, targetBuf);
//            KatanaMod.LOGGER.info("{}", serverPlayerEntity.getName().getString());
//        }
//        KatanaMod.LOGGER.info("applying SkyboundDiamondAbility. Packets sent.");

        //for (ServerPlayerEntity serverPlayerEntity : PlayerLookup.tracking(player)) {
        //    ServerPlayNetworking.send(serverPlayerEntity, KatanaMod.SKYBOUND_S2C_PACKET_ID, playerBuf);
        //}
        //ServerPlayNetworking.send((ServerPlayerEntity) player, KatanaMod.SKYBOUND_S2C_PACKET_ID, passedData);
//        if (target instanceof PlayerEntity) {
//            ServerPlayNetworking.send((ServerPlayerEntity) target, KatanaMod.SET_VELOCITY_PACKET_ID, passedData);
//        }
    }

    @Override
    public Event<OnAttackCallback> getEvent() {
        return OnAttackCallback.POST_HIT_CALLBACK_EVENT;
    }

    @Override
    public OnAttackCallback getFunction() {
        return this::apply;
    }

    private void apply(Entity target, PlayerEntity attacker) {
        ItemStack stack = getKatanaItem().getKatanaStack(attacker, Hand.MAIN_HAND);
        if ((target == null) || (attacker == null) || (stack == null))
            return;

        if (attacker.getWorld() instanceof ClientWorld) {
            KatanaMod.LOGGER.info("Tried to apply SkyboundDiamondAbility in client thread.");
            return;
        }

        if (target instanceof LivingEntity targetLivingEntity) {
            int level = getCurrentLevel(KatanamodItemStackData.getSoulCount(stack));
            int abilityLevel = getAbilityLevel(level);
            if (abilityLevel < 1)
                return;
            apply(stack, targetLivingEntity, attacker, abilityLevel);
        }
    }

    @Override
    public DiamondKatanaItem getKatanaItem() {
        return (DiamondKatanaItem) Items.DIAMOND_KATANA;
    }

    @Override
    public Identifier getIconTexture() {
        return Identifier.ofVanilla("textures/mob_effect/wind_charged.png");
    }

    @Override
    public Text getName() {
        return Text.translatable("katanamod.ability.diamond.skybound.name");
    }

    @Override
    public Text getGenericDescription() {
        return Text.translatable("katanamod.ability.diamond.skybound.description.generic");
    }

    @Override
    public Text getDetailedDescription(int abilityLevel) {
        return Text.translatable("katanamod.ability.diamond.skybound.description.detailed",
                getAttacksToTrigger(abilityLevel),
                toRoman(getJumpBoostLevel(abilityLevel) + 1),
                getJumpBoostTime(abilityLevel),
                getThrowHeight(abilityLevel)
        );
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
        return 6;
    }


    private void apply(ItemStack stack, LivingEntity target, LivingEntity attacker, int abilityLevel) {
        int hitCount = KatanamodItemStackData.getHitCount(stack) + 1;
        if (isTriggered(abilityLevel, hitCount)) {
            hitCount = 0;
            applyAbility(target, attacker, abilityLevel);
        }
        KatanamodItemStackData.setHitCount(stack, hitCount);
    }

    private static boolean isTriggered(int abilityLevel, int hitCount) {
        int currentHitCountTrigger = getAttacksToTrigger(abilityLevel);

        return hitCount >= currentHitCountTrigger;
    }
}