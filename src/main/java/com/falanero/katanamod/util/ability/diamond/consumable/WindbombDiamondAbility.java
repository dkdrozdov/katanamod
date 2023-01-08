package com.falanero.katanamod.util.ability.diamond.consumable;

import com.falanero.katanamod.KatanaMod;
import com.falanero.katanamod.util.ability.ConsumableAbility;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

import static com.falanero.katanamod.util.Utility.arithmeticProgression;
import static com.falanero.katanamod.util.Utility.toRoman;

public class WindbombDiamondAbility {
    public static ConsumableAbility getAbility(){
        return WindbombDiamondAbility::apply;
    }
    private static int getLevel(int itemLevel) {
        return arithmeticProgression(1, 2, 10, itemLevel);
    }
    public static void appendTooltip(int itemLevel, List<Text> tooltip) {
        int abilityLevel = getLevel(itemLevel);
        if (abilityLevel < 1)
            return;

        tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability.windbomb.title", toRoman(abilityLevel)).formatted(Formatting.BOLD));
        tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability.windbomb.description.line_1"));
        tooltip.add(Text.translatable("item.katanamod.diamond_katana.ability.windbomb.description.line_2"));
    }
    public static boolean apply(World world, PlayerEntity user, Hand hand) {
        if (world.isClient)
            return true;

//        boolean sneaking = user.isSneaking();

        ItemStack itemStack = user.getStackInHand(hand);
        user.getItemCooldownManager().set(itemStack.getItem(), 20 * 3);

        Vec3d pos = user.getPos().add(0, -2.5, 0);
        float power = 2.0f;
        float radius = 12.0f;
        float softness = 1.3f;  // (0, 1] has bell shape, [1, +) has helmet shape
        int x1 = MathHelper.floor(pos.x - (double) radius - 1.0);
        int x2 = MathHelper.floor(pos.x + (double) radius + 1.0);
        int y1 = MathHelper.floor(pos.y - (double) radius - 1.0);
        int y2 = MathHelper.floor(pos.y + (double) radius + 1.0);
        int z1 = MathHelper.floor(pos.z - (double) radius - 1.0);
        int z2 = MathHelper.floor(pos.z + (double) radius + 1.0);
        List<Entity> list = world.getOtherEntities(null, new Box(x1, y1, z1, x2, y2, z2));

        for (Entity entity : list) {
            double dx = entity.getX() - pos.x;
            double dy = entity.getEyeY() - pos.y;
            double dz = entity.getZ() - pos.z;
            double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

            if (!entity.isImmuneToExplosion() && !(distance >= radius)) {
                double velocity = Math.pow((-distance * distance + radius * radius) * Math.pow(power, softness) / (radius * radius), 1 / softness);
                double knockbackResistance = 1.0;

                if (entity instanceof LivingEntity livingEntity) {
                    knockbackResistance = Math.max(0.15, 1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                    livingEntity.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.LEVITATION,
                            (int)(20.0*0.4),
                            20), user);
                }
                velocity *= knockbackResistance;

                if (entity instanceof PlayerEntity playerEntity) {
                    if (playerEntity == user)
                        playerEntity.setVelocity(playerEntity.getVelocity().x, power * knockbackResistance, playerEntity.getVelocity().z);
                    else
                        playerEntity.setVelocity(playerEntity.getVelocity().x + dx/distance * velocity, dy/distance * velocity, playerEntity.getVelocity().z + dz/distance * velocity);
                    playerEntity.velocityModified = true;
                    ((ServerPlayerEntity) playerEntity).networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(playerEntity));
                    playerEntity.velocityModified = false;
                } else
                    entity.setVelocity(entity.getVelocity().x + dx/distance * velocity, dy/distance * velocity, entity.getVelocity().z + dz/distance * velocity);

            }
        }
        BlockPos targetPos = new BlockPos(pos);
        PacketByteBuf targetBuf = PacketByteBufs.create();
        targetBuf.writeBlockPos(targetPos);
        for (ServerPlayerEntity serverPlayerEntity : PlayerLookup.tracking((ServerWorld) user.world, targetPos)) {
            ServerPlayNetworking.send(serverPlayerEntity, KatanaMod.WINDBOMB_S2C_PACKET_ID, targetBuf);
//          KatanaMod.LOGGER.info("{}", serverPlayerEntity.getName().getString());
        }
        return true;
    }
}
