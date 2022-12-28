package com.falanero.katanamod.item.katana;

import com.falanero.katanamod.KatanaMod;
import com.falanero.katanamod.callback.*;
import com.falanero.katanamod.entity.FeatherbladeEntity;
import com.falanero.katanamod.registry.Instances;
import com.falanero.katanamod.util.Nbt;
import com.falanero.katanamod.util.Souls;
import com.falanero.katanamod.util.ability.diamond.SkyboundDiamondAbility;
import com.falanero.katanamod.util.ability.diamond.SwiftnessDiamondAbility;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

import static net.minecraft.item.Items.FEATHER;
import static net.minecraft.item.Items.PHANTOM_MEMBRANE;

public class DiamondKatanaItem extends KatanaItem {
    public DiamondKatanaItem(int attackDamage, float attackSpeed, Settings settings) {
        super(attackDamage, attackSpeed, settings);
        ToolBreakCallback.EVENT.register((entity) -> onKatanaBreak(entity, Instances.DIAMOND_SOULGEM, true));
        OnKilledByCallback.EVENT.register(this::onKilledEntity);
        PlayerEntityTickCallback.EVENT.register(this::updateEffect);
        OnAttackCallback.ON_SWEEPING_ATTACK_CALLBACK_EVENT.register(this::sweepingAttack);
        OnAttackCallback.ON_CRIT_ATTACK_CALLBACK_EVENT.register(this::critAttack);
        OnAttackCallback.ON_SPRINT_ATTACK_CALLBACK_EVENT.register(this::sprintAttack);
        OnItemUseCallback.ON_ITEM_USE_CALLBACK.register(this::onItemUse);
        OnGetAirStrafingSpeedCallback.ON_GET_AIR_STRAFING_SPEED_CALLBACK_EVENT.register(this::onGetAirStrafingSpeed);
        OnComputeFallDamage.ON_COMPUTE_FALL_DAMAGE_CALLBACK_EVENT.register(this::onComputeFallDamage);
    }

    private TypedActionResult<Float> onGetAirStrafingSpeed(float airStrafingSpeed, LivingEntity entity) {
        if (entity instanceof PlayerEntity player && isHeldBy(player, null)) {
            int itemLevel = Souls.getCurrentLevel(Nbt.getSoulCount(getStack(player, null)));
            return SwiftnessDiamondAbility.onGetAirStrafingSpeed(airStrafingSpeed, player, itemLevel);
        }
        return new TypedActionResult<>(ActionResult.FAIL, airStrafingSpeed);
    }

    private TypedActionResult<Integer> onComputeFallDamage(int fallDamage, float fallDistance, float damageMultiplier, LivingEntity entity) {
        if (entity instanceof PlayerEntity player && isHeldBy(player, null)) {
            int itemLevel = Souls.getCurrentLevel(Nbt.getSoulCount(getStack(player, null)));
            return SwiftnessDiamondAbility.onComputeFallDamage(fallDamage, fallDistance, damageMultiplier, entity, itemLevel);
        }
        return new TypedActionResult<>(ActionResult.FAIL, fallDamage);
    }

    private void windbombAbilityApply(World world, PlayerEntity user, Hand hand) {
        if (world.isClient)
            return;

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
                entity.velocityModified = true;

            }
        }
        BlockPos targetPos = new BlockPos(pos);
        PacketByteBuf targetBuf = PacketByteBufs.create();
        targetBuf.writeBlockPos(targetPos);
        for (ServerPlayerEntity serverPlayerEntity : PlayerLookup.tracking((ServerWorld) user.world, targetPos)) {
            ServerPlayNetworking.send(serverPlayerEntity, KatanaMod.WINDBOMB_S2C_PACKET_ID, targetBuf);
//          KatanaMod.LOGGER.info("{}", serverPlayerEntity.getName().getString());
        }

    }

    private void featherbladeAbilityApply(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 1F); // plays a globalSoundEvent

        user.getItemCooldownManager().set(this, 5);

        if (!world.isClient) {
            FeatherbladeEntity featherbladeEntity = new FeatherbladeEntity(world, user);
            featherbladeEntity.setItem(itemStack);
            featherbladeEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 0F);
            world.spawnEntity(featherbladeEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1); // decrements itemStack if user is not in creative mode
        }
    }

    private ActionResult onItemUse(World world, PlayerEntity user, Hand hand) {
        KatanaMod.LOGGER.info((user.world.isClient() ? "CLIENT" : "SERVER") + " " + hand.toString() + user.getStackInHand(hand).getItem().getName());
        Item item = user.getStackInHand(hand).getItem();
        if (isHeldBy(user, null)) {
            if (item == PHANTOM_MEMBRANE) {
                windbombAbilityApply(world, user, hand);
                return ActionResult.CONSUME;
            }
            if (item == FEATHER) {
                featherbladeAbilityApply(world, user, hand);
                return ActionResult.CONSUME;
            }
        }
        return ActionResult.PASS;
    }

    private void sweepingAttack(Entity target, PlayerEntity player) {
        if (player == null)
            return;
        if ((isHeldBy(player, Hand.MAIN_HAND)) && !player.world.isClient) {
            ItemStack stack = getStack(player, Hand.MAIN_HAND);
            KatanaMod.LOGGER.info("It's a sweep attack!!!");
        }
    }

    /**
     * @param hand hand to check. If {@code null}, checks both hands.
     * @return if the entity has this item in the given hand.
     */
    static public boolean isHeldBy(LivingEntity entity, Hand hand) {
        if (hand == null)
            return entity.getMainHandStack().getItem() instanceof DiamondKatanaItem ||
                    entity.getOffHandStack().getItem() instanceof DiamondKatanaItem;
        return hand == Hand.MAIN_HAND ?
                entity.getMainHandStack().getItem() instanceof DiamondKatanaItem :
                entity.getOffHandStack().getItem() instanceof DiamondKatanaItem;
    }


    /**
     * @param hand hand to get stack from. If {@code null}, checks both hands.
     * @return the stack of this item the entity has in the given hand, or {@code null} if no such stack is found.
     */
    static public ItemStack getStack(LivingEntity entity, Hand hand) {
        if (hand == Hand.MAIN_HAND) {
            ItemStack mainStack = entity.getMainHandStack();
            return mainStack.getItem() instanceof DiamondKatanaItem ? mainStack : null;
        }
        if (hand == Hand.OFF_HAND) {
            ItemStack offStack = entity.getOffHandStack();
            return offStack.getItem() instanceof DiamondKatanaItem ? offStack : null;
        }
        ItemStack mainStack = entity.getMainHandStack();
        ItemStack offStack = entity.getOffHandStack();
        return mainStack.getItem() instanceof DiamondKatanaItem ? mainStack :
                offStack.getItem() instanceof DiamondKatanaItem ? offStack : null;
    }

    private void critAttack(Entity target, PlayerEntity player) {
        if (player == null)
            return;
        if ((isHeldBy(player, Hand.MAIN_HAND)) && !player.world.isClient) {
            ItemStack stack = getStack(player, Hand.MAIN_HAND);
            KatanaMod.LOGGER.info("It's a crit attack!!!");
        }
    }

    private void sprintAttack(Entity target, PlayerEntity player) {
        if (player == null)
            return;
        if ((isHeldBy(player, Hand.MAIN_HAND)) && !player.world.isClient) {
            ItemStack stack = getStack(player, Hand.MAIN_HAND);
            KatanaMod.LOGGER.info("It's a sprint attack!!!");
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        user.getItemCooldownManager().set(this, 20 * 5);

        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if ((target == null) || (attacker == null) || (stack == null))
            return false;

        int level = Souls.getCurrentLevel(Nbt.getSoulCount(stack));
        SkyboundDiamondAbility.tryApply(stack, target, attacker, level);
        return super.postHit(stack, target, attacker);
    }

    private void debugHorizontalVelocity(PlayerEntity player) {
        if (player.world.isClient()) {
            double x = player.getVelocity().x;
            double z = player.getVelocity().z;
            float hv = (float) Math.sqrt(x * x + z * z);
            player.sendMessage(Text.literal("horizontal velocity: " + String.format("%.2f", hv)), true);
        }
    }

    @Override
    protected void updateEffect(PlayerEntity player) {
        if (player == null || !isHeldBy(player, null))
            return;
//        if(player.world.isClient())
//            player.sendMessage(Text.literal("vertical velocity: " + String.format("%.2f", player.getVelocity().y)), true);

        //- : 0.20 vs 0.12       //  1.67
        //1 : 1.19 vs 0.14       //  8.5
        //2 : 1.39 vs 0.17       //  8.17
        //3 : 1.59 vs 0.19 1.1438//  8.36
        //4 : 1.78 vs 0.21       //  8.47
        //5 : 1.98 vs 0.23 1.2452//  8.25
        //6 : 2.18 vs 0.26       //  8.38
        //7 : 2.38 vs 0.27 1.2020//  8.5
        //8 : 2.58 vs 0.31       //  8.32
        //9 : 2.77 vs 0.33       //  8.39
        //10: 2.97 vs 0.35 1.2478//  8.48571429

        ItemStack stack = getStack(player, null);
        if (isHeldBy(player, null)) {
            int level = Souls.getCurrentLevel(Nbt.getSoulCount(stack));
            SwiftnessDiamondAbility.effectTick(player, level);
        }
    }

    @Override
    protected void onKatanaBreak(LivingEntity entity, Item droppedMaterial, boolean preserveNbt) {
        if (isHeldBy(entity, null) && !entity.world.isClient) {
            super.onKatanaBreak(entity, droppedMaterial, preserveNbt);
        }
    }

    @Override
    protected void onKilledEntity(LivingEntity killer) {
        if (killer == null)
            return;
        if ((isHeldBy(killer, Hand.MAIN_HAND)) && !killer.world.isClient) {
            super.onKilledEntity(killer);
        }
    }

    public void appendTooltipExtra(Pair<ItemStack, List<Text>> callbackContext) {
        Souls.appendTooltipExtraDiamond(callbackContext);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if (Screen.hasShiftDown()) {
            appendTooltipExtra(new Pair<>(itemStack, tooltip));
        } else {
            //Katana description
            super.appendTooltip(itemStack, world, tooltip, tooltipContext);
            //Inlaid katana description
            tooltip.add(Text.translatable("item.katanamod.diamond_katana.tooltip.soulgem"));
            tooltip.add(Text.translatable("item.katanamod.diamond_katana.tooltip.element"));
            //Soul count
            super.appendTooltipSoulCount(itemStack, tooltip);
            //More info
            tooltip.add(Text.translatable("item.katanamod.tooltip_display_more_info").formatted(Formatting.GRAY));
        }
    }

}
